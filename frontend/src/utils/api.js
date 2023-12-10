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

export async function getAllFunctions(setter) {
    const functions = await fetch(BASE_URL + '/functions', CONTENT_TYPE)
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

// Deduction

export async function getDeductions(setter) {
    const deductions = await fetch(BASE_URL + '/deductions', CONTENT_TYPE)
        .then((response) => response.json());
    setter(deductions);
    return deductions;
}

export async function createDeduction(deduction) {
    if(!deduction || !deduction.code || !deduction.account || deduction.code.length != 4) {
        alert('Введите код и счет. Код должен состоять из 4-х символов');
        return;
    }

    let body = {
        "code" : deduction.code,
        "account" : {
            "code" : deduction.account
        },
        "rate" : deduction.rate ? deduction.rate : 0 
    };
    
    const response = await fetch(BASE_URL + '/deduction', {
        ...CONTENT_TYPE,
        method: "POST",
        body: JSON.stringify(body)
    });
    if(response.status === 409 || response.status === 400) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании вычета');
    }
}

export async function deleteDeduction(code) {
    const response = await fetch(BASE_URL + '/deduction/' + code, {
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

// Employee

export async function getEmployees(setter) {
    const employees = await fetch(BASE_URL + '/employees', CONTENT_TYPE)
        .then((response) => response.json());
    setter(employees);
    return employees;
}

export async function createEmployee(employee) {
    if(!employee) {
        alert('Заполните данные сотрудника!');
        return;
    }

    let body = {
        personnelNumber : employee.personnelNumber,
        firstName : employee.firstName,
        lastName : employee.lastName,
        middleName : employee.middleName,
        email : employee.email,
        phone : employee.phone,
        salary : employee.salary,
        function : {
            id : employee.function.id
        }
    };

    const response = await fetch(BASE_URL + '/employee', {
        ...CONTENT_TYPE,
        method: "POST",
        body: JSON.stringify(body)
    });
    if(response.status === 409 || response.status === 400) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании сотрудника');
    }
}

export async function updateEmployee(employee) {
    if(!employee) {
        alert('Заполните данные сотрудника!');
        return;
    }

    let body = {
        firstName : employee.firstName,
        lastName : employee.lastName,
        middleName : employee.middleName,
        email : employee.email,
        phone : employee.phone,
        salary : employee.salary,
        function : {
            id : employee.function.id
        }
    };

    const response = await fetch(BASE_URL + '/employee/' + employee.personnelNumber, {
        ...CONTENT_TYPE,
        method: "PUT",
        body: JSON.stringify(body)
    });
    if(response.status === 400) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при сохранении сотрудника');
    }
}

export async function deleteEmployee(personnelNumber) {
    const response = await fetch(BASE_URL + '/employee/' + personnelNumber, {
        ...CONTENT_TYPE,
        method: "DELETE"
    });
    if(!response.ok) {
        if(response.status === 400 || response.status === 500) {
            const message = await response.json();
            alert('Произошла ошибка при выполнении запроса:\n' + message.message);
        }
        return false;
    }
    return true;
}

// Employee deductions

export async function getEmployeeDeductions(personnelNumber, setter) {
    const employeeDeductions = await fetch(BASE_URL + '/employee/' + personnelNumber + '/deductions', CONTENT_TYPE)
        .then((response) => response.json());
    setter(employeeDeductions);
    return employeeDeductions;
}

export async function createEmployeeDeduction(personnelNumber, deduction) {
    if(!personnelNumber || !deduction) {
        alert('Заполните данные!');
        return;
    }

    let body = {
        code: deduction.code,
        rate: deduction.rate
    };

    const response = await fetch(BASE_URL + '/employee/' + personnelNumber + '/deduction', {
        ...CONTENT_TYPE,
        method: "POST",
        body: JSON.stringify(body)
    });
    if(response.status === 400 || response.status == 409) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании вычета сотрудника');
    }
}

export async function deleteEmployeeDeduction(personnelNumber, code) {
    const response = await fetch(BASE_URL + '/employee/' + personnelNumber + '/deduction/' + code, {
        ...CONTENT_TYPE,
        method: "DELETE"
    });
    if(!response.ok) {
        if(response.status === 400 || response.status === 500) {
            const message = await response.json();
            alert('Произошла ошибка при выполнении запроса:\n' + message.message);
        }
        return false;
    }
    return true;
}

// Timesheet

export async function getTimesheet(personnelNumber, setter) {
    const timesheet = await fetch(BASE_URL + '/employee/' + personnelNumber + '/timesheet', CONTENT_TYPE)
        .then((response) => response.json());
    const result = [];
    for(const el of timesheet) {
        result.push({
            date: `${el.year}-${el.month}-${("0" + el.day).slice(-2)}`,
            present: el.present
        });
    }
    setter(result);
    return result;
}

export async function createTimesheet(personnelNumber, timesheet) {
    if(!personnelNumber || !timesheet) {
        alert('Заполните данные!');
        return;
    }

    const date = timesheet.date;
    let body = {
        year: date.substr(0, 4),
        month: date.substr(5, 2),
        day: date.substr(8, 2),
        present: timesheet.present
    };

    const response = await fetch(BASE_URL + '/employee/' + personnelNumber + '/timesheet', {
        ...CONTENT_TYPE,
        method: "POST",
        body: JSON.stringify(body)
    });
    if(response.status === 400 || response.status == 409) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании табеля рабочего времени');
    }
}

export async function deleteTimesheet(personnelNumber, timesheet) {
    const date = timesheet.date;
    let body = {
        year: date.substr(0, 4),
        month: date.substr(5, 2),
        day: date.substr(8, 2),
        present: timesheet.present
    };

    const response = await fetch(BASE_URL + '/employee/' + personnelNumber + '/timesheet', {
        ...CONTENT_TYPE,
        method: "DELETE",
        body: JSON.stringify(body)
    });

    if(!response.ok) {
        if(response.status === 400 || response.status === 500) {
            const message = await response.json();
            alert('Произошла ошибка при выполнении запроса:\n' + message.message);
        }
        return false;
    }
    return true;
}

// Document 

export async function getDocuments(setter) {
    const documents = await fetch(BASE_URL + '/documents', CONTENT_TYPE)
        .then((response) => response.json());
    documents.sort((a, b) => a.id > b.id ? -1 : 1);
    setter(documents);
    return documents;
}

export async function getDocument(documentId) {
    const document = await fetch(BASE_URL + '/document/' + documentId, CONTENT_TYPE)
        .then((response) => response.json());
    return document;
}

export async function reverseDocument(documentId, date) {
    const response = await fetch(BASE_URL + '/document/' +documentId + '/reverse?posting_date=' + date, {
        ...CONTENT_TYPE,
        method: "POST"
    });
    if(response.status === 400 || response.status == 409) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при сторнировании документа');
    }
}

export async function createDocument(document) {
    if(!document) {
        alert('Заполните данные!');
        return;
    }

    for(const i in document.documentPositions) {
        document.documentPositions[i].account.parent = null;
    }

    const response = await fetch(BASE_URL + '/document', {
        ...CONTENT_TYPE,
        method: "POST",
        body: JSON.stringify(document)
    });
    if(response.status === 400 || response.status == 409) {
        const message = await response.json();
        alert('Произошла ошибка при выполнении запроса:\n' + message.message);
    } else if(!response.ok) {
        alert('Произошла ошибка при создании документа');
    }
    return response.ok;
}

export async function getPayment(personnelNumber) {
    const payment = await fetch(BASE_URL + '/document/payment?id=' + personnelNumber, CONTENT_TYPE)
        .then((response) => response.json());
    return payment;
}

// Reports

export async function getOsv(from, to) {
    const osv = await fetch(BASE_URL + '/report/osv?from=' + from + '&to=' + to, CONTENT_TYPE)
        .then((response) => response.json());
    return osv;
}

export async function getBalance(from, to) {
    const balance = await fetch(BASE_URL + '/report/balance?from=' + from + '&to=' + to, CONTENT_TYPE)
        .then((response) => response.json());
    return balance;
}

export async function getVed(from, to) {
    const ved = await fetch(BASE_URL + '/report/ved?from=' + from + '&to=' + to, CONTENT_TYPE)
        .then((response) => response.json());
    return ved;
}