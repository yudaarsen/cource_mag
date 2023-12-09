import { useEffect, useState, useRef } from 'react';
import '../styles/common.css'
import 'bootstrap/dist/css/bootstrap.css'
import ActionDeleteButton from '../components/ActionDeleteButton'
import ActionCreateButton from '../components/ActionCreateButton';
import { createTimesheet, deleteTimesheet, getTimesheet } from '../utils/api';
import { useParams } from 'react-router-dom';

function Row(props) {   
    function handleChange(e) {
        let nextData = props.data;
        let element = e.target;
        let id = parseInt(element.parentElement.parentElement.getAttribute("name").substring(7));
        const attr = element.getAttribute('name');
        nextData[attr] = element.value;
        if(attr === 'present')
            nextData[attr] = element.checked;
        props.changeData(id, nextData);
    }

    function deleteHandler(e) {
        props.delFunc(props.data);
    }

    function createHandler(e) {
        props.createFunc(props.data);
    }

    const schema = {
        date: { },
        present: { }
    };
    const columns = [];
    for(const [key, val] of Object.entries(schema)) {
        if(key === 'date') {
            columns.push(
                <td>
                {
                    props.editable || props.create ?
                    (
                        <input type='date' className='form-control' name={key} value={props.data[key]} onChange={handleChange}></input>
                        
                    )
                    : <input disabled className="form-control shadow-none no-border" name={key} value={props.data[key]} onChange={handleChange} />
                }
                </td>
            );
        } else if(key === 'present') {
            columns.push(
                <td className='text-center'>
                    <input type='checkbox' className="form-check-input" name={key} checked={props.data[key]} disabled={!props.create} onChange={handleChange} />
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

export default function Timesheet() {
    const [data, setData] = useState([]);
    const params = useParams();
    const createIdx = useRef();

    async function initialize() {
        await getTimesheet(params.personnelNumber, setData);
    }

    useEffect(() => {
       initialize();
    }, []);

    async function handleDelete(timesheet) {
        const result = await deleteTimesheet(params.personnelNumber, timesheet);
        if(!result)
            return;
        data.splice(data.findIndex((element) => element.date === timesheet.date), 1);
        setData([...data]);
    }

    function handleChange(code, elementData) {
        data.splice(data.findIndex((element) => element.code === code), 1, {...elementData});
        setData([...data]);
    }

    async function handleCreate(timesheet) {
        await createTimesheet(params.personnelNumber, timesheet);
        createIdx.current = null;
        initialize();
    }

    function addRow(e) {
        if(createIdx.current)
            return;
        const date = new Date();
        data.push({
            date: `${date.getFullYear()}-${date.getMonth() + 1}-${("0" + date.getDate()).slice(-2)}`
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
            />
        );
    }

    return (
        <div className='container'>
            <div className='m-3'>
            <h1>Табель рабочего времени</h1>
            <div>
                <span>Действия:</span>
                <button type="button" className="m-3 btn btn-success" onClick={addRow}>Создать</button> 
                <a href='/employees' role='button' className='btn btn-dark' aria-pressed="true">Назад</a>
            </div>     
            <table className="table">
                <thead>
                    <tr>
                        <th className='col-2'>Дата</th>
                        <th className='col-1'>Присутствие</th>
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