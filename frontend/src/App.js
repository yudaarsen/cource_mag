import { BrowserRouter, Route, Routes } from "react-router-dom";
import Department from "./catalog/Department";
import Function from "./catalog/Function";
import DocumentType from "./catalog/DocumentType";
import Account from "./catalog/Account";
import Deduction from "./catalog/Deduction";
import Employee from "./catalog/Employee";
import EmployeeDeductions from "./catalog/EmployeeDeductions";
import Timesheet from "./catalog/Timesheet";
import Documents from "./document/Documents";
import DocumentPage from "./document/DocumentPage";
import Main from "./menu/Main";
import Osv from "./reports/Osv";

function App() {
  return (
    <>
    <BrowserRouter>
      <Routes>
        <Route path="/departments" element={ <Department /> }/>
        <Route path="/functions" element={ <Function /> }/>
        <Route path="/document_types" element={ <DocumentType /> }/> 
        <Route path="/accounts" element={ <Account /> } />
        <Route path="/deductions" element={ <Deduction /> } />
        <Route path="/employees" element={ <Employee />} />
        <Route path="/employee/:personnelNumber/deductions" element={ <EmployeeDeductions /> } />
        <Route path="/employee/:personnelNumber/timesheet" element={ <Timesheet /> } />
        <Route path="/documents" element={ <Documents /> } />
        <Route path="/document" element={ <DocumentPage create={true} /> } />
        <Route path="/document/:id" element={ <DocumentPage /> } />
        <Route path="/" element={ <Main /> } />
        <Route path="/report/osv" element={ <Osv /> } />
      </Routes>
    </BrowserRouter>
    </>
  );
}

export default App;
