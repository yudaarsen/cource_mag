export default function ActionCreateButton(props) {
    return <button type="button"
                className="btn btn-success"
                onMouseDown={() => props.setBlur(false)} 
                onMouseUp={props.handleCreate}>
                    Создать
            </button>
}