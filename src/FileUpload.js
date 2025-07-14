import React, { useState } from 'react';
import axios from 'axios';
import './FileUpload.css';

function FileUpload() {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');

  const handleEncrypt = async () => {
    if (!file) return setMessage('Please select a file to encrypt.');
    const formData = new FormData();
    formData.append('file', file);
    try {
      const res = await axios.post('http://localhost:8080/api/files/upload', formData);
      setMessage(res.data);
    } catch (err) {
      setMessage('Encryption failed.');
    }
  };

  const handleDecrypt = async () => {
    if (!file) return setMessage('Please select a .aes file to decrypt.');
    const formData = new FormData();
    formData.append('file', file);
    try {
      const res = await axios.post('http://localhost:8080/api/files/decrypt', formData);
      setMessage(res.data);
    } catch (err) {
      setMessage('Decryption failed.');
    }
  };

  return (
    <div className="file-upload">
      <input type="file" onChange={(e) => setFile(e.target.files[0])} />
      <div className="buttons">
        <button onClick={handleEncrypt}>Encrypt & Upload</button>
        <button onClick={handleDecrypt}>Decrypt & Save</button>
      </div>
      <p className="message">{message}</p>
    </div>
  );
}

export default FileUpload;