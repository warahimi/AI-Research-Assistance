import React from "react";
import { deleteResearch } from "../api/api";

function ResearchDisplayItem({ id, operation, content, onDelete }) {
  const handleDelete = async () => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this note?"
    );
    if (!confirmDelete) return;

    try {
      await deleteResearch(id);
      if (onDelete) onDelete(id); // Trigger parent state update if callback provided
    } catch (err) {
      console.error("Failed to delete note:", err);
      alert("❌ Failed to delete note.");
    }
  };

  return (
    <div className="bg-white p-6 rounded-xl shadow-lg flex flex-col justify-between gap-4 border border-gray-200 hover:shadow-xl transition duration-300">
      <h2 className="text-lg font-semibold text-blue-600 uppercase tracking-wide">
        {operation.replace("_", " ")}
      </h2>

      <p className="text-gray-700 text-sm whitespace-pre-wrap break-words">
        {content}
      </p>

      <button
        type="button"
        className="self-start mt-2 px-4 py-2 bg-red-500 hover:bg-red-600 text-white text-sm rounded-md transition"
        onClick={handleDelete}
      >
        🗑️ Delete Note
      </button>
    </div>
  );
}

export default ResearchDisplayItem;
