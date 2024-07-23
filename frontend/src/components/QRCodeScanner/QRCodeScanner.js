import React, { useState } from "react";
import { Modal, Button } from "antd";
import { Scanner } from "@yudiel/react-qr-scanner";

const QRCodeScanner = ({ value, setValue, onClose, onSwitch }) => {
  const [data, setData] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(true);
  const [openDialog, setOpenDialog] = useState(null);
  const [facingMode, setFacingMode] = useState("user");
  const [scanning, setScanning] = useState(true);

  const handleError = (err) => {
    console.error(err);
  };

  const handleDialogSwitch = (dialogName) => {
    openModal(dialogName);
  };

  const handleDialogClose = () => {
    closeModal();
  };

  const openModal = (dialogName) => {
    setOpenDialog(dialogName);
  };

  const closeModal = () => {
    setOpenDialog(null);
    setScanning(false);
    setFacingMode("");
    onClose();
  };

  const handleCancel = () => {
    setIsModalOpen(false);
    setScanning(false);
    setFacingMode("");
    onClose();
  };

  const onClick = () => {
    setValue(data);
    console.log(value);
    handleCancel();
  };
  return (
    <>
      <Modal
        title="Quét QR Code"
        onCancel={handleCancel}
        open={isModalOpen}
        footer={null}
        centered
        width={"40%"}
      >
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            justifyItems: "center",
          }}
        >
          <div
            style={{
              width: "70%",
              minHeight: "400px",
              justifyContent: "center",
            }}
          >
            <div>
              {scanning && (
                <Scanner onScan={(result) => setData(result[0].rawValue)} />
              )}
            </div>
            {data && (
              <>
                <div
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    marginTop: "5%",
                    maxWidth: "100%",
                  }}
                >
                  <p
                    style={{
                      display: "flex",
                      justifyContent: "center",
                      alignContent: "center",
                      alignItems: "center",
                    }}
                  >
                    Nội dung QR Code: {data}
                  </p>
                </div>

                <div
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    marginTop: "5%",
                  }}
                >
                  <Button onClick={onClick}>Xác nhận</Button>
                </div>
              </>
            )}
          </div>
        </div>
      </Modal>
    </>
  );
};

export default QRCodeScanner;
