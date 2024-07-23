import React from 'react';
import { Modal, Button } from 'antd';

const ConfirmModal = ({ isOpen, handleOk, handleCancel, title, content }) => {
  return (
    <Modal
      title={title}
      open={isOpen}
      onOk={handleOk}
      onCancel={handleCancel}
      footer={[
        <Button key="back" onClick={handleCancel}>
          Hủy bỏ
        </Button>,
        <Button key="submit" type="primary" onClick={handleOk}>
          Xác nhận
        </Button>,
      ]}
      centered
    >
      {content}
    </Modal>
  );
};

export default ConfirmModal;
