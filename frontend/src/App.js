import { BrowserRouter, Route, Routes } from "react-router-dom";
import Department from "./catalog/Department";
import Function from "./catalog/Function";

function App() {
  return (
    <>
    <BrowserRouter>
      <Routes>
        <Route path="/departments" element={ <Department /> }/>
        <Route path="/functions" element={ <Function /> }/>
      </Routes>
    </BrowserRouter>
    </>
  );
}

export default App;
