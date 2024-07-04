import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCookies } from "react-cookie";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Space } from "antd";
import { API, LOGIN, DIALOGS } from "@Const";
import styled from "styled-components";
import theme from "../../../styles/pages/theme";

const LoginDialogStyle = styled.div``;
const LoginDialog = ({ onClose, onSwitch }) => {
  const [cookies] = useCookies(["access_token"]);
  const access_token = cookies.access_token;

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const apiLoginUrl = API.PUBLIC.LOGIN_ENDPOINT;

  const handleButtonCloseClick = () => {
    onClose();
  };

  const handleLoginFormSubmit = () => {};

  const handleSwitchToOtherDialog = () => {};

  return (
    <LoginDialogStyle>
      <div
        className="modal fade show"
        id="modal-auth"
        tabIndex="-1"
        aria-labelledby="exampleModalLabel"
        style={{ display: "block", paddingLeft: "0px" }}
        aria-modal="true"
        role="dialog"
      >
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content">
            <div className="modal-body">
              <div className="title-header-wrap" style={{ marginTop: "5px" }}>
                <span className="title">{LOGIN.LOGIN}</span>
                <button
                  type="button"
                  className="btn-close pointer-cursor"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                  onClick={handleButtonCloseClick}
                ></button>
                <div className="form-wrap">
                  <form
                    method="POST"
                    action={apiLoginUrl}
                    className="form"
                    id="form-login"
                    onSubmit={handleLoginFormSubmit}
                  >
                    <input
                      type="hidden"
                      name="_token"
                      value="kd38LX3442ZoaFGkcWgeVWKJ0xwLrIk5YxQOdqzJ"
                    />
                    <div className="input-wrap">
                      <label className="title">Email</label>
                      <input
                        id="email-login"
                        name="email"
                        type="text"
                        placeholder={LOGIN.EMAIL_PHONE_PLACEHOLDER}
                        onChange={(e) => setEmail(e.target.value)}
                      />
                    </div>
                    <span className="text-danger error-text email-error"></span>
                    <div className="input-wrap input-password-wrap">
                      <label className="title">{LOGIN.PASSWORD}</label>
                      <input
                        id="password-login"
                        name="password"
                        className="input-password"
                        type="password"
                        placeholder={LOGIN.PASSWORD_PLACEHOLDER}
                        onChange={(e) => setPassword(e.target.value)}
                      />
                    </div>
                    <span className="text-danger error-text password-error"></span>
                    <div className="tool-wrap">
                      <span
                        className="title btn-open-fotgot-password"
                        style={{ fontSize: "13px", color: "#285430" }}
                        onClick={() =>
                          handleSwitchToOtherDialog(DIALOGS.FORGOT_PASSWORD)
                        }
                      >
                        {LOGIN.FORGOT_PASSWORD_QUESTION}
                      </span>
                    </div>
                    <div className="btn-wrap">
                      <button
                        type="submit"
                        className="btn btn-primary btn-login"
                      >
                        {LOGIN.LOGIN_BUTTON}
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </LoginDialogStyle>
  );
};

export default LoginDialog;
