import { useEffect, useState, useRef } from 'react';
import '../styles/common.css'
import 'bootstrap/dist/css/bootstrap.css'
import ActionDeleteButton from '../components/ActionDeleteButton'
import ActiveInput from '../components/ActiveInput'
import ActionCreateButton from '../components/ActionCreateButton';
import { createDeduction, deleteDeduction, getAccounts, getDeductions } from '../utils/api';
import Navbar from "../menu/Navbar";

function Row(props) {
    function handleChange(e) {
        let element = e.target;
        let id = parseInt(element.parentElement.parentElement.getAttribute("name").substring(7));
        let nextData = props.data;
        const attr = element.getAttribute('name');
        nextData[attr] = element.value;
        props.changeData(id, nextData);
    }

    function deleteHandler(e) {
        props.delFunc(props.data.code);
    }

    function createHandler(e) {
        props.createFunc(props.data);
    }

    const inputCode = props.editable || props.create ? 
        (<ActiveInput
            value={!props.data.code ? '' : props.data.code}
            name={'code'}
            handleChange={handleChange}
            minLength={4}
            maxLength={4} 
        />)
        : <input disabled className="form-control shadow-none no-border" name='code' value={props.data.code} />
    

    const options = [];
    for(const op in props.accountOptions) {
        options.push(
            <option key={op+1} value={props.accountOptions[op].code}>
                {props.accountOptions[op].code + ' ' + props.accountOptions[op].name}
            </option>
        );
    }
    if(props.create && !props.data.account)
        props.data.account = props.accountOptions[0].code;

    const inputRate = props.editable || props.create ? 
        (<ActiveInput
            value={props.data.rate}
            name={'rate'}
            handleChange={handleChange}
            minLength={0}
            maxLength={4} 
        />)
        : <input disabled className="form-control shadow-none no-border" value={props.data.rate} />

    const inputAccount = props.editable || props.create ? 
    <select name='account' className='form-select' onChange={handleChange}>
        {options}
    </select>
    : <input disabled className="form-control shadow-none no-border" name='account' value={props.data.account.code} />

    let action = <ActionDeleteButton handleDelete={deleteHandler} /> ;
    if(props.create) {
        action = <ActionCreateButton handleCreate={createHandler} setBlur={() => null} />
    }

    return (
        <tr name={"tb_row_" + props.data.code}>
            <td className="text-center" name="id">
                {inputCode}
            </td>
            <td>
                {inputAccount}
            </td>
            <td>
                {inputRate}
            </td>
            <td className="text-center">
                {action}
            </td>
        </tr>
    )
}

export default function Deduction() {
    const [data, setData] = useState([]);
    const [accounts, setAccounts] = useState([]);
    const createIdx = useRef();

    async function initialize() {
        await getDeductions(setData);
        await getAccounts(setAccounts);
    }

    useEffect(() => {
       initialize();
    }, []);

    async function handleDelete(code) {
        const result = await deleteDeduction(code);
        if(!result)
            return;
        data.splice(data.findIndex((element) => element.code === code), 1);
        setData([...data]);
    }

    function handleChange(code, elementData) {
        data.splice(data.findIndex((element) => element.code === code), 1, {...elementData});
        setData([...data]);
    }

    async function handleCreate(deduction) {
        await createDeduction(deduction);
        createIdx.current = null;
        initialize();
    }

    function addRow(e) {
        for(const el of data) {
            if(!el.code)
                return;
        }
        data.push({});
        createIdx.current = data.length - 1;
        setData([...data]);
    }

    const rows = [];
    for(const i in data) {

        rows.push(
            <Row key={i}
                 data={data[i]}
                 editable={false}
                 create={createIdx.current == i}
                 createIdx={createIdx.current}
                 delFunc={handleDelete}
                 createFunc={handleCreate}
                 changeData={handleChange}
                 accountOptions={accounts}
            />
        );
    }

    return (<>
        <Navbar />
        <div className="container">
          <h1>Вычеты</h1>
          <div className='container p-0'>
            <span>Действия:</span>
            <button type="button" className="m-3 btn btn-success" onClick={addRow}>Создать</button> 
          </div>     
          <table className="table">
            <thead>
                <tr>
                    <th className="col-2 text-center">Код</th>
                    <th>Счет</th>
                    <th className="col-2 text-center">Процент, бп</th>
                    <th className="col-1 text-center">Действия</th>
                </tr>
            </thead>
            <tbody className="align-middle">
                {rows} 
            </tbody>
          </table>  
        </div>
    </>)
}