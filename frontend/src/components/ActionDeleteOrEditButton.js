export default function ActionDeleteOrEditButton(props) {
    if(props.rowChange === props.keyVal && props.keyVal > 0)
        return <button type="button" className="btn btn-warning" 
            onMouseDown={() => props.setBlur(false)} 
            onMouseUp={props.handleEdit}>
                Сохранить
        </button>
    return <button type="button" className="btn btn-danger" onClick={props.handleDelete}>Удалить</button>
}