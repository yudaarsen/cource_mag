const BASE_URL = "http://localhost:8080/api/rest";

const CONTENT_TYPE = {
    headers : {
        "Content-Type": "application/json"
    }
}

// Department

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
    if(!response.ok) {
        if(response.status === 400) {
            const message = await response.json();
            alert('Произошла ошибка при выполнении запроса:\n' + message.message);
        }
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

export async function updateDepartment(id, name) {
    if(name.length === 0 || name.length > 50) {
        alert('Длина названия должна быть от 1 до 50 символов!');
        return;
    }
    await fetch(BASE_URL + '/department/' + id, {
        ...CONTENT_TYPE,
        method: "PUT",
        body : JSON.stringify({
            "name" : name
        })
    });
}

// Function

export async function getFunctions(departmentId, setter) {
    const functions = await fetch(BASE_URL + '/functions?department=' + departmentId, CONTENT_TYPE)
        .then((response) => response.json());
    setter(functions);
    return functions;
}

export async function createFunction(name, departmentId) {
    const response = await fetch(BASE_URL + '/function', {
        ...CONTENT_TYPE,
        method: "POST",
        body: JSON.stringify({
            "name" : name,
            "department" : {
                "id" : departmentId
            }
        })
    });
    if(response.status === 400) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании должности');
    }
}

export async function deleteFunction(id) {
    const response = await fetch(BASE_URL + '/function/' + id, {
        ...CONTENT_TYPE,
        method: "DELETE"
    });
    if(!response.ok) {
        if(response.status === 400) {
            const message = await response.json();
            alert('Произошла ошибка при выполнении запроса:\n' + message.message);
        }
        return false;
    }
    return true;
}

export async function updateFunction(id, name) {
    if(name.length === 0 || name.length > 100) {
        alert('Длина названия должна быть от 1 до 1 символов!');
        return;
    }
    await fetch(BASE_URL + '/function/' + id, {
        ...CONTENT_TYPE,
        method: "PUT",
        body : JSON.stringify({
            "name" : name
        })
    });
}

// Document types

export async function getDocumentTypes(setter) {
    const documentTypes = await fetch(BASE_URL + '/document_types', CONTENT_TYPE)
        .then((response) => response.json());
    setter(documentTypes);
    return documentTypes;
} 

export async function createDocumentType(code, name) {
    if(!code || !name || code.length != 4) {
        alert('Введите код и название. Код должен состоять из 4-х символов');
        return;
    }

    const response = await fetch(BASE_URL + '/document_type', {
        ...CONTENT_TYPE,
        method: "POST",
        body: JSON.stringify({
            "code" : code,
            "name" : name
        })
    });
    if(response.status === 409) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании типа документа');
    }
}

export async function deleteDocumentType(code) {
    const response = await fetch(BASE_URL + '/document_type/' + code, {
        ...CONTENT_TYPE,
        method: "DELETE"
    });
    if(!response.ok) {
        if(response.status === 400) {
            const message = await response.json();
            alert('Произошла ошибка при выполнении запроса:\n' + message.message);
        }
        return false;
    }
    return true;
}

// Account

export async function getAccounts(setter) {
    const accounts = await fetch(BASE_URL + '/accounts', CONTENT_TYPE)
        .then((response) => response.json());
    setter(accounts);
    return accounts;
}

export async function createAccount(account) {
    if(!account || !account.code || !account.name || account.code.length != 10) {
        alert('Введите код и название. Код должен состоять из 10-х цифр');
        return;
    }

    let body = {
        "code" : account.code,
        "name" : account.name
    };
    if(account.parent)
        body = {
            ...body,
            parent: {
                code : account.parent
            }
        };

    const response = await fetch(BASE_URL + '/account', {
        ...CONTENT_TYPE,
        method: "POST",
        body: JSON.stringify(body)
    });
    if(response.status === 409 || response.status === 400) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании типа документа');
    }
}

export async function deleteAccount(code) {
    const response = await fetch(BASE_URL + '/account/' + code, {
        ...CONTENT_TYPE,
        method: "DELETE"
    });
    if(!response.ok) {
        if(response.status === 400) {
            const message = await response.json();
            alert('Произошла ошибка при выполнении запроса:\n' + message.message);
        }
        return false;
    }
    return true;
}