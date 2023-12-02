import { BrowserRouter, Route, Routes } from "react-router-dom";
import Department from "./catalog/Department";

function App() {
  return (
    <>
    <BrowserRouter>
      <Routes>
        <Route path="/departments" element={ <Department /> }/>
      </Routes>
    </BrowserRouter>
    </>
  );
}

export default App;
