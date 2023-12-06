import { BrowserRouter, Route, Routes } from "react-router-dom";
import Department from "./catalog/Department";
import Function from "./catalog/Function";
import DocumentType from "./catalog/DocumentType";

function App() {
  return (
    <>
    <BrowserRouter>
      <Routes>
        <Route path="/departments" element={ <Department /> }/>
        <Route path="/functions" element={ <Function /> }/>
        <Route path="/document_types" element={ <DocumentType /> }/> 
      </Routes>
    </BrowserRouter>
    </>
  );
}

export default App;
