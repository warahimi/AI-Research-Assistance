document.addEventListener('DOMContentLoaded', () => {
  // Load saved research notes from local storage
  chrome.storage.local.get(['researchNotes'], (result) => {
    console.log("Loaded saved note:", result.researchNotes);
    if (result.researchNotes) {
      document.getElementById('notes').value = result.researchNotes;
    }
  });

  // Attach button event listeners
  document.getElementById("summarize-button").addEventListener("click", () => processText("SUMMARIZE"));
  document.getElementById("rephrase-button").addEventListener("click", () => processText("REPHRASE"));
  document.getElementById("extract-keywords-button").addEventListener("click", () => processText("EXTRACT_KEYWORDS"));
  document.getElementById("suggest-button").addEventListener("click", () => processText("SUGGEST"));
  document.getElementById("suggest-button").addEventListener("click", () => processText("SUGGEST"));
  document.getElementById("rephrase-button").addEventListener("click", () => processText("REPHRASE"));
  document.getElementById("translate-button").addEventListener("click", () => processText("TRANSLATE"));
  document.getElementById("detect-sentiment-button").addEventListener("click", () => processText("DETECT_SENTIMENT"));

  document.getElementById("savenotes-button").addEventListener("click", saveNotes);
});

// Process selected text with operation: SUMMARIZE, REPHRASE, etc.
async function processText(operation) {
  try {
    const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
    const result = await chrome.scripting.executeScript({
      target: { tabId: tab.id },
      function: () => window.getSelection().toString()
    });

    const selectedText = result[0]?.result?.trim();
    console.log("Selected text:", selectedText);

    if (!selectedText) {
      showResult("No text selected. Please highlight some text.");
      return;
    }

    const response = await fetch('http://localhost:8080/api/v1/researches', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ content: selectedText, operation })
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.status}`);
    }

    const text = await response.text();
    showResult(text.replace(/\n/g, '<br>'), operation);
  } catch (error) {
    console.error('Processing error:', error);
    showResult('Error: ' + error.message);
  }
}

// Save notes from textarea to local storage
async function saveNotes() {
  const notes = document.getElementById('notes').value;
  console.log("Saving note:", notes);
  try {
    await chrome.storage.local.set({ researchNotes: notes });
    document.getElementById('notes').value = notes; // re-apply for clarity
    showResult('Notes saved successfully!', 'notes');
  } catch (error) {
    console.error('Error saving notes:', error);
    showResult('Error saving notes: ' + error.message, 'notes');
  }
}

// Display result message in the results section
function showResult(result, type = "summary") {
  document.getElementById('results').innerHTML = `
    <div class="result-item ${type.toLowerCase()}">
      <div class="result-content"><p>${result}</p></div>
    </div>`;
}
