export default function ActiveInput(props) {
    return (
        <input className="form-control shadow-none no-border"
                name={props.name}
                onFocus={props.handleFocus} 
                onBlur={props.handleBlur}
                onChange={props.handleChange} 
                value = {props.value}
        />
    );
}