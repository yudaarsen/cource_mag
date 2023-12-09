export default function ActiveInput(props) {
    return (
        <input className="form-control shadow-none no-border"
                maxLength={props.maxLength}
                minLength={props.minLength}
                name={props.name}
                onFocus={props.handleFocus} 
                onBlur={props.handleBlur}
                onChange={props.handleChange} 
                value = {props.value}
        />
    );
}