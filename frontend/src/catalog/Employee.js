import { useEffect, useState, useRef } from 'react';
import '../styles/common.css'
import 'bootstrap/dist/css/bootstrap.css'
import ActionDeleteButton from '../components/ActionDeleteButton'
import ActiveInput from '../components/ActiveInput'
import ActionCreateButton from '../components/ActionCreateButton';
import ActionDeleteOrEditButton from '../components/ActionDeleteOrEditButton';
import { createEmployee, deleteEmployee, getAllFunctions, getDepartments, getEmployees, updateEmployee } from '../utils/api';
import Navbar from "../menu/Navbar";

function Row(props) {
    const [rowChange, setRowChange] = useState();
    const [blur, setBlur] = useState(true);
    const oldValue = useRef();

    function onFocus(e) {
        oldValue.current = {...props.data};
        let key = parseInt(e.target.parentElement.parentElement.getAttribute("name").substring(7));
        setRowChange(key);
    }

    function onBlur(e) {
        if(blur && !props.create) {
            setRowChange(0);
            let element = e.target;
            let id = parseInt(element.parentElement.parentElement.getAttribute("name").substring(7));
            props.changeData(id, oldValue.current);
        }
    }

    function editHandler(e) {
        let id = parseInt(e.target.parentElement.parentElement.getAttribute("name").substring(7));
        let result = props.saveFunc(props.data);
        if(!result) {
            props.changeData(id, oldValue.current);
        }
        setBlur(true);
        setRowChange(0);
    }

    function handleChange(e) {
        let element = e.target;
        let id = parseInt(element.parentElement.parentElement.getAttribute("name").substring(7));
        let nextData = props.data;
        const attr = element.getAttribute('name');
        if(attr === 'department') {
            const func = props.functions.find((el) => el.department.id == element.value);
            nextData.function = func;
        } else if(attr === 'function') {
            const func = props.functions.find((el) => el.id == element.value);
            nextData.function = {
                id: element.value,
                name: func.name,
                department: func.department
            }
        } else {
            nextData[attr] = element.value;
        }
        props.changeData(id, nextData);
    }

    function deleteHandler(e) {
        props.delFunc(props.data.personnelNumber);
    }

    function createHandler(e) {
        props.createFunc(props.data);
    }

    const schema = {
        personnelNumber: {
            minLength: 1,
            maxLength: null
        },
        firstName: {
            minLength: 1,
            maxLength: 100
        },
        lastName: {
            minLength: 1,
            maxLength: 100
        },
        middleName: {
            minLength: 1,
            maxLength: 100
        },
        department: {
            lst: props.departments
        },
        function: {
            lst : props.functions
        },
        phone: {
            minLength: 1,
            maxLength: 15
        },
        email: {
            minLength: 1,
            maxLength: 255
        },
        salary: {
            minLength: 1,
            maxLength: null
        }
    };
    const columns = [];
    for(const [key, val] of Object.entries(schema)) {
        const pass = key === 'personnelNumber';
        if(val.minLength || val.maxLength) {
            columns.push(
                <td>
                {
                    props.editable && !pass || props.create ?
                    (<ActiveInput handleFocus={onFocus}
                        handleBlur={onBlur}
                        value={!props.data[key] ? '' : props.data[key]}
                        name={key}
                        handleChange={handleChange}
                        minLength={val.minLength}
                        maxLength={val.maxLength}
                    />)
                    : <input disabled className="form-control shadow-none no-border" name={key} value={props.data[key]} />
                }
                </td>
            );
        } else if('lst' in val) {
            const options = [];
            for(const op in val.lst) {
                if(key === 'function' && props.data.function.department.id != val.lst[op].department.id)
                    continue;
                let selected = false;
                if(key === 'department') {
                    selected = val.lst[op].id == props.data.function.department.id
                } else if(key === 'function') {
                    selected = val.lst[op].id == props.data.function.id
                }
                options.push(
                    <option key={op+1} value={val.lst[op].id} selected={selected}> 
                        {val.lst[op].name}
                    </option>
                );
            }
           
            const value = key === 'department' && props.data.function 
                ? props.data.function.department.name 
                : props.data.function.name;
            columns.push(
                <td>
                    { 
                        props.editable || props.create  ? (
                            <select  name={key} className='form-select' onChange={handleChange} onFocus={onFocus} onBlur={onBlur}>
                                {options}
                            </select> 
                        ) 
                        : <input disabled className="form-control shadow-none no-border" name={key} value={value} />
                    }
                </td>
            );
        }
    }

    let action = props.editable ? 
        <ActionDeleteOrEditButton rowChange={rowChange} keyVal={props.data.personnelNumber} handleEdit={editHandler} handleDelete={deleteHandler} setBlur={setBlur} />
        : <ActionDeleteButton handleDelete={deleteHandler} />
    if(props.create) {
        action = <ActionCreateButton handleCreate={createHandler} setBlur={() => null} />
    }

    const actions = [];
    actions.push(action);
    if(!props.create) {
        actions.push(
            <a href={'/employee/' + props.data.personnelNumber + '/deductions'} role='button' aria-pressed="true" className='btn btn-info m-2'>
                Вычеты
            </a>
        );
        actions.push(
            <a href={'/employee/' + props.data.personnelNumber + '/timesheet'} role='button' aria-pressed="true" className='btn btn-dark'>
                Табель
            </a>
        );
    }

    return (
        <tr name={"tb_row_" + props.data.personnelNumber}>
            {columns}
            <td className="text-center">
                {actions}
            </td>
        </tr>
    )
}

export default function Employee() {
    const [data, setData] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [functions, setFunctions] = useState([]);
    const createIdx = useRef();

    async function initialize() {
        await getDepartments(setDepartments);
        await getAllFunctions(setFunctions);
        await getEmployees(setData);
    }

    useEffect(() => {
       initialize();
    }, []);

    async function handleDelete(personnelNumber) {
        const result = await deleteEmployee(personnelNumber);
        if(!result)
            return;
        data.splice(data.findIndex((element) => element.personnelNumber === personnelNumber), 1);
        setData([...data]);
    }

    function handleChange(personnelNumber, elementData) {
        data.splice(data.findIndex((element) => element.personnelNumber === personnelNumber), 1, {...elementData});
        setData([...data]);
    }

    async function handleCreate(employee) {
        await createEmployee(employee);
        createIdx.current = null;
        initialize();
    }

    async function handleUpdate(employee) {
        await updateEmployee(employee);
        initialize();
    }

    function addRow(e) {
        for(const el of data) {
            if(!el.personnelNumber)
                return;
        }
        data.push({
            function: functions[0],
            department: functions[0].department
        });
        createIdx.current = data.length - 1;
        setData([...data]);
    }

    const rows = [];
    for(const i in data) {

        rows.push(
            <Row key={i}
                 data={data[i]}
                 editable={true}
                 create={createIdx.current == i}
                 createIdx={createIdx.current}
                 delFunc={handleDelete}
                 createFunc={handleCreate}
                 changeData={handleChange}
                 departments={departments}
                 functions={functions}
                 saveFunc={handleUpdate}
            />
        );
    }

    return (<>
        <Navbar />
        <div className='m-3'>
          <h1>Сотрудники</h1>
          <div>
            <span>Действия:</span>
            <button type="button" className="m-3 btn btn-success" onClick={addRow}>Создать</button> 
          </div>     
          <table className="table">
            <thead>
                <tr>
                    <th className='col-1'>Табельный номер</th>
                    <th>Имя</th>
                    <th>Фамилия</th>
                    <th>Отчество</th>
                    <th className='col-2'>Подразделение</th>
                    <th className='col-2'>Должность</th>
                    <th>Телефон</th>
                    <th>Email</th>
                    <th>Зарплата</th>
                    <th className='col-2'>Действия</th>
                </tr>
            </thead>
            <tbody className="align-middle">
                {rows} 
            </tbody>
          </table>  
        </div>
    </>)
}