import React from "react";
import ResearchDisplayItem from "./ResearchDisplayItem";

function ResearchDisplayList({ researches, setResearches }) {
  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-center text-blue-700 mb-6">
        Saved Researches
      </h1>

      {researches.length === 0 ? (
        <p className="text-center text-gray-600 text-lg">
          No research notes saved yet.
        </p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {researches.map((research) => (
            // <ResearchDisplayItem key={research.id} {...research} />
            <ResearchDisplayItem
              key={research.id}
              {...research}
              onDelete={(deletedId) =>
                setResearches((prev) => prev.filter((r) => r.id !== deletedId))
              }
            />
          ))}
        </div>
      )}
    </div>
  );
}

export default ResearchDisplayList;
