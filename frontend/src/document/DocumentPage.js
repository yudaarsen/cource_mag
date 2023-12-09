import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createDocument, getAccounts, getDocument, getDocumentTypes, getEmployees, getPayment } from "../utils/api";
import { getDateFormat } from "../utils/utils";
import '../styles/common.css'
import Navbar from "../menu/Navbar";

function TypeField(props) {
    const options = [];
    for(const el of props.types) {
        options.push(
            <option value={el.code} key={el.code}>
                {el.name}
            </option>
        );
    }

    const type = props.type;

    return props.create ? (
        <select className="form-select" disabled={!props.create} onChange={props.handleChange} value={props.value}>
            {options}
        </select>
    ) 
    : <>
        {type?.name}
      </>
}

export default function DocumentPage(props) {
    const [documentType, setDocumentType] = useState();
    const [postingDate, setPostingDate] = useState(getDateFormat(new Date()));
    const [note, setNote] = useState('');
    const [reverse, setReverse] = useState('');
    const [positions, setPositions] = useState([]);
    
    const [documentTypes, setDocumentTypes] = useState([]);
    const [accounts, setAccounts] = useState([]); 
    const [employees, setEmployees] = useState([]);
    const params = useParams();
    const navigate = useNavigate();

    const [autoPaym, setAutoPaym] = useState();

    async function initialize() {
        await getDocumentTypes(setDocumentTypes);
        await getAccounts(setAccounts);
        await getEmployees(setEmployees);
        if(!props.create) {
            const document = await getDocument(params.id);
            setDocumentType(document.documentType);
            setReverse(document.reverseDocument);
            setPostingDate(document.postingDate);
            setNote(document.note);
            setPositions(document.documentPositions);
        }
    } 

    useEffect(() => {
        initialize();
    }, []);

    function addPosition(e) {
        setPositions([
            ...positions, 
            {
                posType: 'D',
                account: accounts[0],
                amount: 0,
                employee: null,
                note: ''
            }
        ]);
    }

    function handleChange(e) {
        const element = e.target;
        const name = element.getAttribute('name');
        const idx = parseInt(element.parentElement.parentElement.getAttribute("name"));
        const nextPos = positions[idx];
        if(name == 'account') {
            nextPos[name] = accounts.find((el) => el.code == element.value);
        } else if(name == 'employee') {
            if(!nextPos[name])
                nextPos[name] = {personnelNumber : null}
            nextPos[name]['personnelNumber'] = element.value;
        }
        else
            nextPos[name] = element.value;
        positions.splice(idx, 1, nextPos);
        setPositions([...positions]);
    }

    function handleDelete(e) {
        const element = e.target;
        const idx = parseInt(element.parentElement.parentElement.getAttribute("name"));
        positions.splice(idx, 1);
        setPositions([...positions]);
    }

    async function handleCreate(e) {
        const document = {
            documentType: {
                code: documentType ? documentType : documentTypes[0].code
            },
            postingDate: postingDate,
            note: note,
            documentPositions: positions
        }
        const res = await createDocument(document);
        if(res) {
            navigate('/documents');
        }
    }

    async function handleAuto(e) {
        const payment = await getPayment(autoPaym);
        if(!payment.length)
            return 
        for(const pos of payment) {
            positions.push({
                posType: pos.posType,
                account: pos.account ? pos.account : accounts[0],
                amount: pos.amount,
                employee: pos.employee,
                note: pos.note
            });
        }
        setPositions([...positions]);
    }

    const title = props.create ? 'Создать документ' : 'Документ №' + params.id;

    const accountsRepr = [];
    for(const i in accounts) {
        accountsRepr.push(
            <option key={i} value={accounts[i].code}>
                {accounts[i].code + ' ' + accounts[i].name}
            </option> 
        );
    }

    const employeeRepr = [];
    employeeRepr.push(<option key={0}>Нет</option>);
    for(const i in employees) {
        employeeRepr.push(
            <option key={i + 1} value={employees[i].personnelNumber}>
                {
                    employees[i].personnelNumber + ' ' 
                    + employees[i].lastName 
                    + ' ' 
                    + employees[i].firstName 
                    + ' ' 
                    + employees[i].middleName 
                }
            </option>
        );
    }

    const rows = [];
    for(const i in positions) {
        const position = positions[i];
        rows.push(
            <tr key={i} name={i}>
                <td>
                    <select className="form-select no-border" disabled={!props.create} name="posType" value={position.posType} onChange={handleChange}>
                        <option value={'C'}>Кредит</option>
                        <option value={'D'}>Дебет</option>
                    </select>
                </td>
                <td>
                    <select className="form-select no-border" disabled={!props.create} name="account" value={position.account.code} onChange={handleChange}>
                        {accountsRepr}
                    </select>
                </td>
                <td>
                    <input min="1" className="form-control no-border" disabled={!props.create} name="amount" type="number" value={position.amount} onChange={handleChange} />
                </td>
                <td>
                    <select className="form-select no-border" disabled={!props.create} name="employee" value={position.employee?.personnelNumber} onChange={handleChange}>
                        {employeeRepr}
                    </select>
                </td>
                <td>
                    <input className="form-control no-border" type="text" disabled={!props.create} name="note" value={position.note} onChange={handleChange} />
                </td>
                { 
                    props.create
                    ? ( 
                        <td className="text-center">
                            <button className="btn btn-danger" onClick={handleDelete}>Удалить</button>
                        </td>
                    ) 
                    : ''
                }
            </tr>
        );
    }

    return (<>
        <Navbar />
        <div className="m-3">
          <h1>{title}</h1>
          <div className='p-0 mt-3'>
            <a href='/documents' role='button' className='btn btn-dark m-1' aria-pressed="true">Назад</a>
            { 
                props.create 
                ? (
                    <button type="button" className="btn btn-success" onClick={handleCreate}> 
                        Провести
                    </button> 
                ) 
                : '' 
            }           
            <div className="row mb-3 mt-3">
                <div className="col col-2">
                    Тип документа:
                </div>
                <div className="col col-3">
                    <TypeField type={documentType} types={documentTypes} create={props.create} handleChange={(e) => setDocumentType(e.target.value)} value={documentType} />
                </div>
                {
                    props.create 
                    ? (<>
                        <div className="col col-2 offset-2">
                            Выбор сотрудника для авторасчета:
                        </div>
                        <div className="col col-2">
                            <select className="form-select" value={autoPaym} onChange={(e) => setAutoPaym(e.target.value)}>
                                {employeeRepr}
                            </select>
                        </div>
                       </>
                    )
                    : ''
                }
            </div>

            <div className="row mb-3 mt-3">
                <div className="col col-2">
                    Дата проводки:
                </div>
                <div className="col col-3">
                    <input className="form-control" type="date" disabled={!props.create} value={postingDate} onChange={(e) => setPostingDate(e.target.value)} />
                </div>
                {
                    props.create 
                    ? (
                        <div className="col col-2 offset-2">
                            <button type="button" className="btn btn-warning" onClick={handleAuto}>Авторасчет</button>
                        </div>
                    )
                    : ''
                }
            </div>

            <div className="row mb-3 mt-3">
                <div className="col col-2">
                    Примечание:
                </div>
                <div className="col col-3">
                    <input type="text" maxLength={250} className="form-control" disabled={!props.create} value={note} onChange={(e) => setNote(e.target.value)} />
                </div>
            </div>

            {
                !props.create 
                ? (<div className="row mb-3 mt-3">
                        <div className="col col-2">
                            Документ сторно:
                        </div>
                        <div className="col col-3">
                            {reverse}
                        </div>
                    </div>
                ) 
                : '' 
            }
          </div>
          { 
            props.create 
            ? (
                <button type="button" className="btn btn-info mb-2" onClick={addPosition}> 
                    Добавить позицию
                </button> 
            ) 
            : '' 
          }     
          <table className="table">
            <thead>
                <tr>
                    <th className="col-1">Тип позиции</th>
                    <th>Счет</th>
                    <th>Сумма</th>
                    <th>Сотрудник</th>
                    <th>Примечание</th>
                    {
                        props.create ?
                            <th>Действия</th>
                        : ''
                    }
                </tr>
            </thead>
            <tbody className="align-middle">
                {rows}
            </tbody>
          </table>  
        </div>
    </>);
}