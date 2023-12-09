import { useState } from 'react';
import Navbar from '../menu/Navbar';
import '../styles/common.css'
import { getOsv } from '../utils/api';
import { getDateFormat } from '../utils/utils';

export default function Osv() {
    const [data, setData] = useState({positions: []});
    const [from, setFrom] = useState();
    const [to, setTo] = useState(getDateFormat(new Date()));

    async function loadOsv() {
        if(!from || !to) {
            alert('Выберите период!');
            return;
        }
        setData(await getOsv(from, to));
    }

    const rows = [];
    for(const el of data.positions) {
        rows.push(
            <tr key={el.account.code}>
                <td>
                    {el.account.code + ' ' + el.account.name}
                </td>
                <td>
                    {el.startDebit.toFixed(2)}
                </td>
                <td>
                    {el.startCredit.toFixed(2)}
                </td>
                <td>
                    {el.periodDebit.toFixed(2)}
                </td>
                <td>
                    {el.periodCredit.toFixed(2)}
                </td>
                <td>
                    {el.endDebit.toFixed(2)}
                </td>
                <td>
                    {el.endCredit.toFixed(2)}
                </td>
            </tr>
        );
    }

    rows.push(
        <tr key={'Total'}>
            <td>
                <b>
                    Итого
                </b>
            </td>
            <td>
                <b>
                    {data.prevTotals?.debit.toFixed(2)}
                </b>
            </td>
            <td>
                <b>
                    {data.prevTotals?.credit.toFixed(2)}
                </b>
            </td>
            <td>
                <b>
                    {data.periodTotals?.debit.toFixed(2)}
                </b>
            </td>
            <td>
                <b>
                    {data.periodTotals?.credit.toFixed(2)}
                </b>
            </td>
            <td>
                <b>
                    {data.endTotals?.debit.toFixed(2)}
                </b>
            </td>
            <td>
                <b>
                    {data.endTotals?.credit.toFixed(2)}
                </b>
            </td>
        </tr>
    )


    return (<>
        <Navbar />
        <div className='m-3'>
            <h1>Оборотно-сальдовая ведомость</h1>

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
                    <tr>
                        <td rowSpan="2" className='col-2'>
                            <b>
                                Счет
                            </b>
                        </td>

                        <td colSpan="2">
                            <b>
                                Сальдо на начало периода
                            </b>
                        </td>

                        <td colSpan="2">
                            <b>
                                Обороты за период
                            </b>
                        </td>

                        <td colSpan="2">
                            <b>
                                Сальдо на конец периода
                            </b>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <b>
                                Дебет
                            </b>
                        </td>
                        <td>
                            <b>
                                Кредит
                            </b>
                        </td>
                        
                        <td>
                            <b>
                                Дебет
                            </b>
                        </td>
                        <td>
                            <b>
                                Кредит
                            </b>
                        </td>

                        <td>
                            <b>
                                Дебет
                            </b>
                        </td>
                        <td>
                            <b>
                                Кредит
                            </b>
                        </td>
                    </tr>
                </thead>
                <tbody>
                    {rows}
                </tbody>
            </table>
        </div>
    </>);
}