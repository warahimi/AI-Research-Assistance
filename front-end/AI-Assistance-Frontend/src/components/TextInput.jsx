import React, { useState } from "react";
import { processRequest, saveResearch } from "../api/api";

function TextInput() {
  const [text, setText] = useState("");
  const [result, setResult] = useState("");
  const [operation, setOperation] = useState("");
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  const handleOperation = async (operationType) => {
    if (!text.trim()) {
      alert("Please enter some text before processing.");
      return;
    }

    setOperation(operationType);
    setLoading(true);
    try {
      const response = await processRequest(operationType, text);
      setResult(response);
    } catch (err) {
      console.error("Processing error:", err);
      setResult("❌ Failed to process request.");
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!result.trim()) {
      alert("There is no result to save.");
      return;
    }

    setSaving(true);
    try {
      await saveResearch(operation, result);
      alert("✅ Research saved!");
    } catch (err) {
      console.error("Save error:", err);
      alert("❌ Failed to save research.");
    } finally {
      setSaving(false);
    }
  };

  const operationButtons = [
    "SUMMARIZE",
    "SUGGEST",
    "REPHRASE",
    "TRANSLATE",
    "EXTRACT_KEYWORDS",
    "DETECT_SENTIMENT",
  ];

  return (
    <div className="max-w-5xl mx-auto p-4">
      <h1 className="text-3xl font-bold text-center my-6 text-blue-700">
        Research Assistant
      </h1>

      {/* Input Textarea */}
      <label htmlFor="input" className="font-semibold text-lg block mb-2">
        Enter Text to Process:
      </label>
      <textarea
        id="input"
        className="w-full h-56 p-3 border border-gray-300 rounded-lg mb-6"
        placeholder="Paste or type your research content here..."
        onChange={(e) => setText(e.target.value)}
        value={text}
      />

      {/* Operation Buttons */}
      <div className="flex flex-wrap gap-4 justify-center mb-6">
        {operationButtons.map((op) => (
          <button
            key={op}
            className="px-4 py-3 bg-blue-500 hover:bg-blue-600 text-white rounded-lg text-sm font-semibold transition"
            onClick={() => handleOperation(op)}
            disabled={loading}
          >
            {loading && operation === op
              ? "Processing..."
              : op.replace("_", " ")}
          </button>
        ))}
      </div>

      {/* Result Textarea */}
      <label htmlFor="result" className="font-semibold text-lg block mb-2">
        Processed Result:
      </label>
      <textarea
        id="result"
        className="w-full h-56 p-3 border border-gray-300 rounded-lg mb-4"
        value={result}
        onChange={(e) => setResult(e.target.value)}
        placeholder="Result will appear here..."
      />

      {/* Save Button */}
      <form onSubmit={handleSave} className="text-center">
        <button
          type="submit"
          className="px-6 py-3 bg-green-600 hover:bg-green-700 text-white font-semibold rounded-lg transition"
          disabled={saving}
        >
          {saving ? "Saving..." : "💾 Save Research"}
        </button>
      </form>
    </div>
  );
}

export default TextInput;
