const BASE_URL = "http://localhost:8080/api/rest";

const CONTENT_TYPE = {
    headers : {
        "Content-Type": "application/json"
    }
}

export async function getDepartments(setter) {
    const response = await fetch(BASE_URL + '/departments', CONTENT_TYPE).then((response) => response);
    if(response.status !== 200) {
        alert('Ошибка при выполнении запроса');
        return [];
    }
    const data = await response.json((data) => data);
    setter(data);
    return data;
}

export async function deleteDepartment(id) {
    const response = await fetch(BASE_URL + '/department/' + id, {
        ...CONTENT_TYPE,
        method: "DELETE"
    });
    if(response.status === 400) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
        return false;
    }
    return true;
}

export async function createDepartment(name) {
    if(name.length === 0 || name.length > 50) {
        alert('Длина названия должна быть от 1 до 50 символов!');
        return;
    }
    const response = await fetch(BASE_URL + '/department', {
        ...CONTENT_TYPE,
        method: "POST",
        body : JSON.stringify({
            "name" : name
        })
    });
    if(response.status === 409) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании подразделения');
    }
}