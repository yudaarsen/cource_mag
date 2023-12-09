import { useEffect, useState, useRef } from 'react';
import '../styles/common.css'
import 'bootstrap/dist/css/bootstrap.css'
import ActionDeleteButton from '../components/ActionDeleteButton'
import ActiveInput from '../components/ActiveInput'
import ActionCreateButton from '../components/ActionCreateButton';
import ActionDeleteOrEditButton from '../components/ActionDeleteOrEditButton';
import { createEmployee, createEmployeeDeductin, createEmployeeDeduction, deleteEmployee, deleteEmployeeDeduction, getAllFunctions, getDeductions, getDepartments, getEmployeeDeductions, getEmployees, updateEmployee } from '../utils/api';
import { useParams } from 'react-router-dom';

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

    const schema = {
        code: {
            lst: props.deductions
        },
        rate: {
            minLength: 1,
            maxLength: 4
        }
    };
    const columns = [];
    for(const [key, val] of Object.entries(schema)) {
        if(val.minLength || val.maxLength) {
            columns.push(
                <td>
                {
                    props.editable || props.create ?
                    (<ActiveInput 
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
                options.push(
                    <option key={op+1} value={val.lst[op].code}> 
                        {val.lst[op].code}
                    </option>
                );
            }
           
            const value = props.data.code;
            columns.push(
                <td>
                    { 
                        props.editable || props.create  ? (
                            <select  name={key} className='form-select' onChange={handleChange}>
                                {options}
                            </select> 
                        ) 
                        : <input disabled className="form-control shadow-none no-border" name={key} value={value} />
                    }
                </td>
            );
        }
    }

    let action = <ActionDeleteButton handleDelete={deleteHandler} />
    if(props.create) {
        action = <ActionCreateButton handleCreate={createHandler} setBlur={() => null} />
    }

    return (
        <tr name={"tb_row_" + props.data.personnelNumber}>
            {columns}
            <td className="text-center">
                {action}
            </td>
        </tr>
    )
}

export default function EmployeeDeductions() {
    const [data, setData] = useState([]);
    const [deductions, setDeductions] = useState([]);
    const params = useParams();
    const createIdx = useRef();

    async function initialize() {
        await getDeductions(setDeductions);
        await getEmployeeDeductions(params.personnelNumber, setData);
    }

    useEffect(() => {
       initialize();
    }, []);

    async function handleDelete(code) {
        const result = await deleteEmployeeDeduction(params.personnelNumber, code);
        if(!result)
            return;
        data.splice(data.findIndex((element) => element.code === code), 1);
        setData([...data]);
    }

    function handleChange(code, elementData) {
        data.splice(data.findIndex((element) => element.code === code), 1, {...elementData});
        setData([...data]);
    }

    async function handleCreate(employeeDeductions) {
        await createEmployeeDeduction(params.personnelNumber, employeeDeductions);
        createIdx.current = null;
        initialize();
    }

    function addRow(e) {
        if(createIdx.current)
            return;
        data.push({
            code: deductions[0].code
        });
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
                 deductions={deductions}
            />
        );
    }

    return (
        <div className='container'>
            <div className='m-3'>
            <h1>Вычеты сотрудника</h1>
            <div>
                <span>Действия:</span>
                <button type="button" className="m-3 btn btn-success" onClick={addRow}>Создать</button> 
                <a href='/employees' role='button' className='btn btn-dark' aria-pressed="true">Назад</a>
            </div>     
            <table className="table">
                <thead>
                    <tr>
                        <th className='col-3'>Код вычета</th>
                        <th className='col-3'>Процент, бп</th>
                        <th className='col-2'>Действия</th>
                    </tr>
                </thead>
                <tbody className="align-middle">
                    {rows} 
                </tbody>
            </table>  
            </div>
        </div>
    )
}