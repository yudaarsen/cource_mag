import { useState } from 'react';
import Navbar from '../menu/Navbar';
import '../styles/common.css'
import { getVed } from '../utils/api';
import { getDateFormat } from '../utils/utils';

export default function Ved() {
    const [data, setData] = useState([]);
    const [from, setFrom] = useState();
    const [to, setTo] = useState(getDateFormat(new Date()));

    async function loadOsv() {
        if(!from || !to) {
            alert('Выберите период!');
            return;
        }
        const ved = await getVed(from, to);
        setData(ved);
    }

    const rows = [];
    for(const el of data) {
        rows.push(
            <tr key={el.personnelNumber}>
                <td>
                    {el.personnelNumber}
                </td>
                <td>
                    {el.fio}
                </td>
                <td>
                    {el.fname}
                </td>
                <td>
                    {el.salary}
                </td>
                <td>
                    {el.present}
                </td>
            </tr>
        );
    }

    return (<>
        <Navbar />
        <div className='container'>
            <h1>Расчетная ведомость</h1>

            <div className='row container my-3'>
                <div className='col col-2'>
                    Начало периода
                </div>
                <div className='col col-2'>
                    <input type='date' value={from} onChange={(e) => setFrom(e.target.value)} className='form-control' />
                </div>
            </div>

            <div className='row container my-3'>
                <div className='col col-2'>
                    Конец периода
                </div>
                <div className='col col-2'>
                    <input type='date' value={to} onChange={(e) => setTo(e.target.value)} className='form-control' />
                </div>
            </div>

            <div className='row container my-3'>
                <div className='col col-2'>
                    <button type='button' className='btn btn-primary' onClick={loadOsv}>Сформировать отчет</button>
                </div>
            </div>

            <table className="table">
                <thead className='align-middle text-center'>
                    <th>
                        <b>
                            Табльный номер
                        </b>
                    </th>

                    <th>
                        <b>
                            ФИО
                        </b>
                    </th>

                    <th>
                        <b>
                            Должность
                        </b>
                    </th>

                    <th>
                        <b>
                            Зарплата
                        </b>
                    </th>

                    <th>
                        <b>
                            Отработано дней
                        </b>
                    </th>
                </thead>
                <tbody>
                    {rows}
                </tbody>
            </table>
        </div>
    </>);
}