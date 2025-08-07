import React, { useEffect, useState } from "react";
import { Routes, Route, Link } from "react-router-dom";
import { getResearches } from "./api/api";
import TextInput from "./components/TextInput";
import ResearchDisplayList from "./components/ResearchDisplayList";

function App() {
  const [researches, setResearches] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await getResearches(); // ✅ fix typo from `date` to `data`
        setResearches(data);
      } catch (error) {
        console.error("Error fetching researches:", error);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 p-4">
      <nav className="flex justify-center gap-6 mb-6">
        <Link
          to="/"
          className="px-4 py-2 rounded-md bg-blue-500 text-white font-semibold hover:bg-blue-600 transition duration-200"
        >
          Home
        </Link>
        <Link
          to="/saved-researches"
          className="px-4 py-2 rounded-md bg-green-500 text-white font-semibold hover:bg-green-600 transition duration-200"
        >
          Saved Researches
        </Link>
      </nav>

      <Routes>
        <Route path="/" element={<TextInput />} />
        <Route
          path="/saved-researches"
          element={
            <ResearchDisplayList
              researches={researches}
              setResearches={setResearches}
            />
          }
        />
      </Routes>
    </div>
  );
}

export default App;
