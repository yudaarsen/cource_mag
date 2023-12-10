import { useEffect, useState } from "react";
import { getDocuments, reverseDocument } from "../utils/api";
import { getDateFormat } from "../utils/utils";
import Navbar from "../menu/Navbar";


export default function Documents() {
    const [data, setData] = useState([]);
    const [reverseDate, setReverseDate] = useState(getDateFormat(new Date()));

    async function initialize() {
        await getDocuments(setData);
    } 

    useEffect(() => {
        initialize();
    }, []);

    async function handleReverse(e) {
        const element = e.target;
        const id = parseInt(element.parentElement.parentElement.getAttribute('name'));
        await reverseDocument(id, reverseDate);
        initialize();
    }

    const rows = [];
    for(const i in data) {
        const doc = data[i];
        rows.push(
            <tr key={i} name={doc.id}>
                <td>{doc.id}</td>
                <td>{doc.documentType.name}</td>
                <td>{doc.postingDate}</td>
                <td>{doc.reverseDocument == 0 ? '-' : doc.reverseDocument}</td>
                <td>{doc.note}</td>
                <td className="text-center">
                    <a href={'/document/' + doc.id} role='button' aria-pressed="true" className='btn btn-primary'>
                        Открыть
                    </a>
                    <button type="button" className="btn btn-danger m-1" onClick={handleReverse}>Сторнировать</button> 
                </td>
            </tr>
        );
    }
    
    return (<>
        <Navbar />
        <div className="container">
          <h1>Документы</h1>
          <div className='container p-0'>
            <span>Действия:</span>
            <a href={'/document'} role='button' aria-pressed="true" className='m-3 btn btn-success'>
                Создать
            </a>
            <a href={'/report/osv'} role='button' aria-pressed="true" className='btn btn-info'>
                Оборотно-сальдовая ведомость
            </a>
            <a href={'/report/balance'} role='button' aria-pressed="true" className='btn m-3 btn-info'>
                Баланс
            </a>
            <div className="row mb-3">
                <div className="col col-2">
                    <span>Дата сторнирования:</span>
                </div>
                <div className="col col-3">
                    <input className="form-control" type="date" value={reverseDate} onChange={(e) => setReverseDate(e.target.value)}/>
                </div>
            </div>
          </div>     
          <table className="table">
            <thead>
                <tr>
                    <th>Номер</th>
                    <th>Тип документа</th>
                    <th>Дата проводки</th>
                    <th>Документ сторно</th>
                    <th className="col-3">Примечание</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody className="align-middle">
                {rows} 
            </tbody>
          </table>  
        </div>
    </>)
}