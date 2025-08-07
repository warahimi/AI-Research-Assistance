export const API_BASE_URL = 'http://localhost:8080/api/v1/researches';

export const getResearches = async () => {
  const response = await fetch(`${API_BASE_URL}`);
  if (!response.ok) {
    throw new Error('Failed to fetch researches');
  }
  return response.json();
}

export const deleteResearch = async (id) => {
  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) {
    throw new Error('Failed to delete research');
  }
  return response.json();
}

export const processRequest = async (operationType, text) => {
  const response = await fetch(`${API_BASE_URL}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ operation: operationType, content: text }),
  });
  if (!response.ok) {
    throw new Error('Failed to process request');
  }
  return response.text();
}

export const saveResearch = async (operationType, text) => {
  const response = await fetch(`${API_BASE_URL + '/save-note'}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ operation: operationType, content: text }),
  });
  if (!response.ok) {
    throw new Error('Failed to save research');
  }
  return response.json();
} 