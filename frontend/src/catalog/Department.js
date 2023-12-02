import { useEffect, useRef, useState } from 'react';
import '../styles/common.css'
import 'bootstrap/dist/css/bootstrap.css'
import ActionDeleteButton from '../components/ActionDeleteButton'
import ActionDeleteOrEditButton from '../components/ActionDeleteOrEditButton'
import ActiveInput from '../components/ActiveInput'
import ActionCreateButton from '../components/ActionCreateButton';
import { createDepartment, deleteDepartment, getDepartments } from '../utils/api';

function Row(props) {
    const [rowChange, setRowChange] = useState();
    const [blur, setBlur] = useState(true);
    const oldValue = useRef();

    function handleChange(e) {
        let element = e.target;
        let id = parseInt(element.parentElement.parentElement.getAttribute("name").substring(7));
        let nextData = props.data;
        nextData.name = element.value; 
        props.changeData(id, nextData);
    }

    function onFocus(e) {
        oldValue.current = {...props.data};
        let key = parseInt(e.target.parentElement.parentElement.getAttribute("name").substring(7))
        setRowChange(key);
    }

    function onBlur(e) {
        if(blur) {
            setRowChange(0);
            let element = e.target;
            let id = parseInt(element.parentElement.parentElement.getAttribute("name").substring(7));
            props.changeData(id, oldValue.current);
        }
    }

    function editHandler(e) {
        let id = parseInt(e.target.parentElement.parentElement.getAttribute("name").substring(7));
        let result = props.saveFunc(id);
        if(!result) {
            props.changeData(id, oldValue.current);
        }
        setBlur(true);
        setRowChange(0);
    }

    function deleteHandler(e) {
        const id = parseInt(e.target.parentElement.parentElement.getAttribute("name").substring(7));
        props.delFunc(id);
    }

    function createHandler(e) {
        const name = e.target.parentElement.parentElement.querySelector('input[name="dep_name"]').value;
        props.createFunc(name);
    }

    let action = props.editable ? 
        <ActionDeleteOrEditButton rowChange={rowChange} keyVal={props.data.id} handleEdit={editHandler} setBlur={setBlur} />
        : <ActionDeleteButton handleDelete={deleteHandler} />

    if(isNaN(props.data.id)) {
        action = <ActionCreateButton handleCreate={createHandler} setBlur={setBlur} />
    }

    const input = props.editable || isNaN(props.data.id) ? 
        (<ActiveInput handleFocus={onFocus}
            handleBlur={onBlur}
            handleChange={handleChange}
            value={props.data.name}
            name={'dep_name'} 
        />)
        : <input disabled className="form-control shadow-none no-border" value={props.data.name} />

    return (
        <tr name={"tb_row_" + props.data.id}>
            <td className="text-center" name="dep_id">{isNaN(props.data.id) ? '' : props.data.id}</td>
            <td>
                {input}
            </td>
            <td className="text-center">
                {action}
            </td>
        </tr>
    )
}

function Department() {
    const [data, setData] = useState([]);

    useEffect(() => {
        getDepartments(setData);
    }, []);

    async function handleDelete(id) {
        const result = await deleteDepartment(id);
        if(!result)
            return;
        data.splice(data.findIndex((element) => element.id === id), 1);
        setData([...data]);
    }

    function handleChange(id, elementData) {
        data.splice(data.findIndex((element) => element.id === id), 1, {id: id, name : elementData.name});
        setData([...data]);
    }


    async function handleCreate(name) {
        await createDepartment(name);
        await getDepartments(setData);
    }

    function addRow(e) {
        data.push({id : NaN, name: ""});
        setData([...data]);
    }

    const rows = [];
    for(const el of data) {
        rows.push(
            <Row key={el.id}
                 data={el}
                 changeData={handleChange}
                 editable={false}
                 delFunc={handleDelete}
                 createFunc={handleCreate} 
            />
        );
    }

    return (
        <div className="container">
          <h1>Подразделения</h1>
          <div className='container p-0'>
            <span>Действия:</span>
            <button type="button" className="m-3 btn btn-success" onClick={addRow}>Создать</button> 
          </div>     
          <table className="table">
            <thead>
                <tr>
                    <th className="col-1 text-center">Номер</th>
                    <th>Наименование</th>
                    <th className="col-1 text-center">Действия</th>
                </tr>
            </thead>
            <tbody className="align-middle">
                {rows} 
            </tbody>
          </table>  
        </div>
    )
}

export default Department;