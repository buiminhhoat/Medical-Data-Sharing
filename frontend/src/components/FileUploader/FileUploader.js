import React, { useEffect, useState, useMemo } from "react";
import { Upload, Button, message } from "antd";
import { UploadOutlined } from "@ant-design/icons";
import axios from "axios";

const FileUploader = ({ hashFile, setHashFile }) => {
  const JWT =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mb3JtYXRpb24iOnsiaWQiOiIxYWYxY2VkMy1lOWZkLTQ0OWYtYWM0Ni03NWMzYWFhNTViOTQiLCJlbWFpbCI6Im9mZmljaWFsLmJ1aW1pbmhob2F0QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwaW5fcG9saWN5Ijp7InJlZ2lvbnMiOlt7ImlkIjoiRlJBMSIsImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxfSx7ImlkIjoiTllDMSIsImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxfV0sInZlcnNpb24iOjF9LCJtZmFfZW5hYmxlZCI6ZmFsc2UsInN0YXR1cyI6IkFDVElWRSJ9LCJhdXRoZW50aWNhdGlvblR5cGUiOiJzY29wZWRLZXkiLCJzY29wZWRLZXlLZXkiOiI1MjJkODVkZjJlN2YyODg0ZDZkZiIsInNjb3BlZEtleVNlY3JldCI6ImRlZTFhOWNmZjIxMzE3OTdlOTU0MmNhYWRiNzEyZWY1NzU4Yzg3M2JmMzZjZmUwODc1ZGUyMDMwNWQxNjY4YWYiLCJpYXQiOjE3MjA1NzM1MTV9.AKcgQTwqr2pca3bOP3LWiBUIWcTAWIAdmtlw9R_W-NU";
  const [fileList, setFileList] = useState([]);

  const handleChange = (info) => {
    let newFileList = [...info.fileList];

    // Update the state
    setFileList(newFileList);
  };

  const beforeUpload = (file) => {
    if (fileList.length >= 1) {
      message.error("Chỉ được tải lên 1 file");
    }
    return true;
  };

  const handleUpload = async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    console.log(file);

    const pinataMetadata = JSON.stringify({
      name: file.uid,
    });
    formData.append("pinataMetadata", pinataMetadata);

    const pinataOptions = JSON.stringify({
      cidVersion: 0,
    });
    formData.append("pinataOptions", pinataOptions);

    try {
      const response = await axios.post(
        "https://api.pinata.cloud/pinning/pinFileToIPFS",
        formData,
        {
          maxBodyLength: "Infinity",
          headers: {
            "Content-Type": `multipart/form-data; boundary=${formData._boundary}`,
            Authorization: `Bearer ${JWT}`,
          },
        }
      );

      if (response.status === 200) {
        console.log(response.data);
        const newFileList = fileList.map((fl) => {
          if (fl.uid === file.uid) {
            return { ...fl, status: "done" };
          }
          return fl;
        });
        setFileList(newFileList);
        setHashFile(response.data.IpfsHash);
        message.success("Đã tải file thành công!");
      }
    } catch (error) {
      console.log(error);
      const newFileList = fileList.map((fl) => {
        if (fl.uid === file.uid) {
          return { ...fl, status: "error" };
        }
        return fl;
      });
      setFileList(newFileList);
      message.error("Đã gặp lỗi trong quá trình upload file!");
    }
  };

  return (
    <Upload
      customRequest={({ file }) => handleUpload(file)}
      accept=".jpg,.png,.gif,.pdf,.zip,.rar"
      maxCount="1"
      fileList={fileList}
      onChange={handleChange}
      beforeUpload={beforeUpload}
    >
      <Button icon={<UploadOutlined />}>Click to Upload</Button>
    </Upload>
  );
};

export default FileUploader;
