import { useState } from 'react';
import '../styles/common.css'
import 'bootstrap/dist/css/bootstrap.css'


function ActionDeleteOrEditButton(props) {
    if(props.rowChange == props.keyVal && props.keyVal > 0)
        return <button type="button" className="btn btn-warning" 
            onMouseDown={() => props.setBlur(false)} 
            onMouseUp={() => {props.handleEdit()}}>
                Сохранить
        </button>
    return <button type="button" className="btn btn-danger">Удалить</button>
}

function Department() {
    const [rowChange, setRowChange] = useState();
    const [blur, setBlur] = useState(true);

    function onFocus(el, key) {
        key = parseInt(el.target.parentElement.parentElement.getAttribute("name").substring(7))
        setRowChange(key);
    }

    function onBlur(el) {
        if(blur)
            setRowChange(0);
    }

    function editHandler() {
        setBlur(true);
        setRowChange(0);
    }

    return (
        <div className="container">
          <h1>Заголовок</h1>
          <div className='container p-0'>
            <span>Действия:</span>
            <button type="button" className="m-3 btn btn-success">Создать</button> 
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
                <tr name="tb_row_1">
                    <td className="text-center">1</td>
                    <td>
                        <input className="form-control shadow-none no-border" name="dep_name" onFocus={(e) => onFocus(e)} onBlur={(e) => onBlur(e)} value = "Администрация"/>
                    </td>
                    <td className="text-center">
                        <ActionDeleteOrEditButton rowChange={rowChange} keyVal={1} handleEdit={editHandler} setBlur={setBlur} />
                    </td>
                </tr>
                <tr name="tb_row_2">
                    <td className="text-center">2</td>
                    <td>
                        <input className="form-control shadow-none no-border" name="dep_name" onFocus={(e) => onFocus(e)} onBlur={(e) => onBlur(e)} value = "Производственный отдел"/>
                    </td>
                    <td className="text-center">
                    <ActionDeleteOrEditButton rowChange={rowChange} keyVal={2} handleEdit={editHandler} setBlur={setBlur} />
                    </td>
                </tr>
            </tbody>
          </table>  
        </div>
    )
}

export default Department;