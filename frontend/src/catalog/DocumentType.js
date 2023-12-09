import { useEffect, useState, useRef } from 'react';
import '../styles/common.css'
import 'bootstrap/dist/css/bootstrap.css'
import ActionDeleteButton from '../components/ActionDeleteButton'
import ActiveInput from '../components/ActiveInput'
import ActionCreateButton from '../components/ActionCreateButton';
import { createDocumentType, deleteDocumentType, getDocumentTypes } from '../utils/api';
import Navbar from "../menu/Navbar";


function Row(props) {
    function handleChange(e) {
        let element = e.target;
        let id = parseInt(element.parentElement.parentElement.getAttribute("name").substring(7));
        let nextData = props.data;
        if(element.getAttribute('name') === 'code') 
            nextData.code = element.value; 
        else
            nextData.name = element.value; 
        props.changeData(id, nextData);
    }

    function deleteHandler(e) {
        const code = e.target.parentElement.parentElement.querySelector('input[name="code"]').value;
        props.delFunc(code);
    }

    function createHandler(e) {
        const code = e.target.parentElement.parentElement.querySelector('input[name="code"]').value;
        const name = e.target.parentElement.parentElement.querySelector('input[name="name"]').value;
        props.createFunc(code, name);
    }

    const inputName = props.editable || props.create ? 
        (<ActiveInput
            value={props.data.name}
            name={'name'}
            handleChange={handleChange}
            minLength={1}
            maxLength={50} 
        />)
        : <input disabled className="form-control shadow-none no-border" value={props.data.name} />

    const inputCode = props.editable || props.create ? 
        (<ActiveInput
            value={!props.data.code ? '' : props.data.code}
            name={'code'}
            handleChange={handleChange}
            minLength={4}
            maxLength={4} 
        />)
        : <input disabled className="form-control shadow-none no-border" name='code' value={props.data.code} />
    
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
                {inputName}
            </td>
            <td className="text-center">
                {action}
            </td>
        </tr>
    )
}

export default function DocumentType() {
    const [data, setData] = useState([]);
    const createIdx = useRef();

    async function initialize() {
        await getDocumentTypes(setData);
    }

    useEffect(() => {
       initialize();
    }, []);

    async function handleDelete(code) {
        const result = await deleteDocumentType(code);
        if(!result)
            return; 
        data.splice(data.findIndex((element) => element.code === code), 1);
        setData([...data]);
    }

    function handleChange(code, elementData) {
        data.splice(data.findIndex((element) => element.code === code), 1, {code: elementData.code, name : elementData.name});
        setData([...data]);
    }

    async function handleCreate(code, name) {
        await createDocumentType(code, name);
        createIdx.current = null;
        initialize();
    }

    function addRow(e) {
        for(const el of data) {
            if(!el.code)
                return;
        }
        data.push({code : NaN, name: ""});
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
                 delFunc={handleDelete}
                 createFunc={handleCreate}
                 changeData={handleChange}
            />
        );
    }

    return (<>
        <Navbar />
        <div className="container">
          <h1>Типы документов</h1>
          <div className='container p-0'>
            <span>Действия:</span>
            <button type="button" className="m-3 btn btn-success" onClick={addRow}>Создать</button> 
          </div>     
          <table className="table">
            <thead>
                <tr>
                    <th className="col-1 text-center">Код</th>
                    <th>Наименование</th>
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