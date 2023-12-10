import { useState } from 'react';
import Navbar from '../menu/Navbar';
import '../styles/common.css'
import { getBalance, getOsv } from '../utils/api';
import { getDateFormat } from '../utils/utils';

export default function Balance() {
    const [data, setData] = useState([]);
    const [from, setFrom] = useState();
    const [to, setTo] = useState(getDateFormat(new Date()));

    async function loadOsv() {
        if(!from || !to) {
            alert('Выберите период!');
            return;
        }
        const balance = await getBalance(from, to);
        setData(balance);
    }

    const rows = [];
    for(const el of data) {
        rows.push(
            <tr key={el.code}>
                <td>
                    {el.name.includes('Итого') || el.name.toLowerCase().includes('баланс') ? <b>{el.name}</b> : el.name}
                </td>
                <td>
                    {el.code}
                </td>
                <td>
                    {el.start}
                </td>
                <td>
                    {el.end}
                </td>
            </tr>
        );
    }

    return (<>
        <Navbar />
        <div className='container'>
            <h1>Бухгалтерский баланс</h1>

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
                            Наименование показателя
                        </b>
                    </th>

                    <th>
                        <b>
                            Код
                        </b>
                    </th>

                    <th>
                        <b>
                            Начало периода
                        </b>
                    </th>

                    <th>
                        <b>
                            Конец периода
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