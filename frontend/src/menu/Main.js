import Navbar from "./Navbar";

export default function Main() {
    return (<>
                <Navbar />
                <div className="container">
                    <h1>Справочники</h1>
                    <div class="list-group">
                        <a href="/departments" class="list-group-item list-group-item-action">Подразделения</a>
                        <a href="/functions" class="list-group-item list-group-item-action">Должности</a>
                        <a href="/document_types" class="list-group-item list-group-item-action">Типы документов</a>
                        <a href="/accounts" class="list-group-item list-group-item-action">План счетов</a>
                        <a href="/deductions" class="list-group-item list-group-item-action">Вычеты</a>
                    </div>
                </div>
            </>
    )
}