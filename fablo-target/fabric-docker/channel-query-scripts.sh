#!/usr/bin/env bash

source "$FABLO_NETWORK_ROOT/fabric-docker/scripts/channel-query-functions.sh"

set -eu

channelQuery() {
  echo "-> Channel query: " + "$@"

  if [ "$#" -eq 1 ]; then
    printChannelsHelp

  elif [ "$1" = "list" ] && [ "$2" = "patient" ] && [ "$3" = "peer0" ]; then

    peerChannelListTls "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "patient" ] && [ "$3" = "peer1" ]
  then

    peerChannelListTls "cli.patient.healthcare.com" "peer1.patient.healthcare.com:7042" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "patient" ] && [ "$3" = "peer2" ]
  then

    peerChannelListTls "cli.patient.healthcare.com" "peer2.patient.healthcare.com:7043" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "doctor" ] && [ "$3" = "peer0" ]
  then

    peerChannelListTls "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "doctor" ] && [ "$3" = "peer1" ]
  then

    peerChannelListTls "cli.doctor.healthcare.com" "peer1.doctor.healthcare.com:7062" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "doctor" ] && [ "$3" = "peer2" ]
  then

    peerChannelListTls "cli.doctor.healthcare.com" "peer2.doctor.healthcare.com:7063" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "pharmacy" ] && [ "$3" = "peer0" ]
  then

    peerChannelListTls "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "pharmacy" ] && [ "$3" = "peer1" ]
  then

    peerChannelListTls "cli.pharmacy.healthcare.com" "peer1.pharmacy.healthcare.com:7082" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "pharmacy" ] && [ "$3" = "peer2" ]
  then

    peerChannelListTls "cli.pharmacy.healthcare.com" "peer2.pharmacy.healthcare.com:7083" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "laboratory" ] && [ "$3" = "peer0" ]
  then

    peerChannelListTls "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "laboratory" ] && [ "$3" = "peer1" ]
  then

    peerChannelListTls "cli.laboratory.healthcare.com" "peer1.laboratory.healthcare.com:7102" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "laboratory" ] && [ "$3" = "peer2" ]
  then

    peerChannelListTls "cli.laboratory.healthcare.com" "peer2.laboratory.healthcare.com:7103" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "insurance" ] && [ "$3" = "peer0" ]
  then

    peerChannelListTls "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "insurance" ] && [ "$3" = "peer1" ]
  then

    peerChannelListTls "cli.insurance.healthcare.com" "peer1.insurance.healthcare.com:7122" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "insurance" ] && [ "$3" = "peer2" ]
  then

    peerChannelListTls "cli.insurance.healthcare.com" "peer2.insurance.healthcare.com:7123" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif

    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "patient" ] && [ "$4" = "peer0" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "patient" ] && [ "$5" = "peer0" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.patient.healthcare.com" "$TARGET_FILE" "peer0.patient.healthcare.com:7041" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "patient" ] && [ "$5" = "peer0" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.patient.healthcare.com" "${BLOCK_NAME}" "peer0.patient.healthcare.com:7041" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "patient" ] && [ "$4" = "peer1" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.patient.healthcare.com" "peer1.patient.healthcare.com:7042" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "patient" ] && [ "$5" = "peer1" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.patient.healthcare.com" "$TARGET_FILE" "peer1.patient.healthcare.com:7042" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "patient" ] && [ "$5" = "peer1" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.patient.healthcare.com" "${BLOCK_NAME}" "peer1.patient.healthcare.com:7042" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "patient" ] && [ "$4" = "peer2" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.patient.healthcare.com" "peer2.patient.healthcare.com:7043" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "patient" ] && [ "$5" = "peer2" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.patient.healthcare.com" "$TARGET_FILE" "peer2.patient.healthcare.com:7043" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "patient" ] && [ "$5" = "peer2" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.patient.healthcare.com" "${BLOCK_NAME}" "peer2.patient.healthcare.com:7043" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "doctor" ] && [ "$4" = "peer0" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "doctor" ] && [ "$5" = "peer0" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.doctor.healthcare.com" "$TARGET_FILE" "peer0.doctor.healthcare.com:7061" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "doctor" ] && [ "$5" = "peer0" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.doctor.healthcare.com" "${BLOCK_NAME}" "peer0.doctor.healthcare.com:7061" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "doctor" ] && [ "$4" = "peer1" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.doctor.healthcare.com" "peer1.doctor.healthcare.com:7062" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "doctor" ] && [ "$5" = "peer1" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.doctor.healthcare.com" "$TARGET_FILE" "peer1.doctor.healthcare.com:7062" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "doctor" ] && [ "$5" = "peer1" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.doctor.healthcare.com" "${BLOCK_NAME}" "peer1.doctor.healthcare.com:7062" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "doctor" ] && [ "$4" = "peer2" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.doctor.healthcare.com" "peer2.doctor.healthcare.com:7063" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "doctor" ] && [ "$5" = "peer2" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.doctor.healthcare.com" "$TARGET_FILE" "peer2.doctor.healthcare.com:7063" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "doctor" ] && [ "$5" = "peer2" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.doctor.healthcare.com" "${BLOCK_NAME}" "peer2.doctor.healthcare.com:7063" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "pharmacy" ] && [ "$4" = "peer0" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "pharmacy" ] && [ "$5" = "peer0" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.pharmacy.healthcare.com" "$TARGET_FILE" "peer0.pharmacy.healthcare.com:7081" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "pharmacy" ] && [ "$5" = "peer0" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.pharmacy.healthcare.com" "${BLOCK_NAME}" "peer0.pharmacy.healthcare.com:7081" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "pharmacy" ] && [ "$4" = "peer1" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.pharmacy.healthcare.com" "peer1.pharmacy.healthcare.com:7082" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "pharmacy" ] && [ "$5" = "peer1" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.pharmacy.healthcare.com" "$TARGET_FILE" "peer1.pharmacy.healthcare.com:7082" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "pharmacy" ] && [ "$5" = "peer1" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.pharmacy.healthcare.com" "${BLOCK_NAME}" "peer1.pharmacy.healthcare.com:7082" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "pharmacy" ] && [ "$4" = "peer2" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.pharmacy.healthcare.com" "peer2.pharmacy.healthcare.com:7083" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "pharmacy" ] && [ "$5" = "peer2" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.pharmacy.healthcare.com" "$TARGET_FILE" "peer2.pharmacy.healthcare.com:7083" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "pharmacy" ] && [ "$5" = "peer2" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.pharmacy.healthcare.com" "${BLOCK_NAME}" "peer2.pharmacy.healthcare.com:7083" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "laboratory" ] && [ "$4" = "peer0" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "laboratory" ] && [ "$5" = "peer0" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.laboratory.healthcare.com" "$TARGET_FILE" "peer0.laboratory.healthcare.com:7101" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "laboratory" ] && [ "$5" = "peer0" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.laboratory.healthcare.com" "${BLOCK_NAME}" "peer0.laboratory.healthcare.com:7101" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "laboratory" ] && [ "$4" = "peer1" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.laboratory.healthcare.com" "peer1.laboratory.healthcare.com:7102" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "laboratory" ] && [ "$5" = "peer1" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.laboratory.healthcare.com" "$TARGET_FILE" "peer1.laboratory.healthcare.com:7102" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "laboratory" ] && [ "$5" = "peer1" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.laboratory.healthcare.com" "${BLOCK_NAME}" "peer1.laboratory.healthcare.com:7102" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "laboratory" ] && [ "$4" = "peer2" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.laboratory.healthcare.com" "peer2.laboratory.healthcare.com:7103" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "laboratory" ] && [ "$5" = "peer2" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.laboratory.healthcare.com" "$TARGET_FILE" "peer2.laboratory.healthcare.com:7103" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "laboratory" ] && [ "$5" = "peer2" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.laboratory.healthcare.com" "${BLOCK_NAME}" "peer2.laboratory.healthcare.com:7103" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "insurance" ] && [ "$4" = "peer0" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "insurance" ] && [ "$5" = "peer0" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.insurance.healthcare.com" "$TARGET_FILE" "peer0.insurance.healthcare.com:7121" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "insurance" ] && [ "$5" = "peer0" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.insurance.healthcare.com" "${BLOCK_NAME}" "peer0.insurance.healthcare.com:7121" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "insurance" ] && [ "$4" = "peer1" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.insurance.healthcare.com" "peer1.insurance.healthcare.com:7122" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "insurance" ] && [ "$5" = "peer1" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.insurance.healthcare.com" "$TARGET_FILE" "peer1.insurance.healthcare.com:7122" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "insurance" ] && [ "$5" = "peer1" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.insurance.healthcare.com" "${BLOCK_NAME}" "peer1.insurance.healthcare.com:7122" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "healthcare-channel" ] && [ "$3" = "insurance" ] && [ "$4" = "peer2" ]
  then

    peerChannelGetInfoTls "healthcare-channel" "cli.insurance.healthcare.com" "peer2.insurance.healthcare.com:7123" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "insurance" ] && [ "$5" = "peer2" ]; then
    TARGET_FILE=${6:-"$channel-config.json"}

    peerChannelFetchConfigTls "healthcare-channel" "cli.insurance.healthcare.com" "$TARGET_FILE" "peer2.insurance.healthcare.com:7123" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$3" = "healthcare-channel" ] && [ "$4" = "insurance" ] && [ "$5" = "peer2" ]; then
    BLOCK_NAME=$2
    TARGET_FILE=${6:-"$BLOCK_NAME.block"}

    peerChannelFetchBlockTls "healthcare-channel" "cli.insurance.healthcare.com" "${BLOCK_NAME}" "peer2.insurance.healthcare.com:7123" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "$TARGET_FILE"

  else

    echo "$@"
    echo "$1, $2, $3, $4, $5, $6, $7, $#"
    printChannelsHelp
  fi

}

printChannelsHelp() {
  echo "Channel management commands:"
  echo ""

  echo "fablo channel list patient peer0"
  echo -e "\t List channels on 'peer0' of 'patient'".
  echo ""

  echo "fablo channel list patient peer1"
  echo -e "\t List channels on 'peer1' of 'patient'".
  echo ""

  echo "fablo channel list patient peer2"
  echo -e "\t List channels on 'peer2' of 'patient'".
  echo ""

  echo "fablo channel list doctor peer0"
  echo -e "\t List channels on 'peer0' of 'doctor'".
  echo ""

  echo "fablo channel list doctor peer1"
  echo -e "\t List channels on 'peer1' of 'doctor'".
  echo ""

  echo "fablo channel list doctor peer2"
  echo -e "\t List channels on 'peer2' of 'doctor'".
  echo ""

  echo "fablo channel list pharmacy peer0"
  echo -e "\t List channels on 'peer0' of 'pharmacy'".
  echo ""

  echo "fablo channel list pharmacy peer1"
  echo -e "\t List channels on 'peer1' of 'pharmacy'".
  echo ""

  echo "fablo channel list pharmacy peer2"
  echo -e "\t List channels on 'peer2' of 'pharmacy'".
  echo ""

  echo "fablo channel list laboratory peer0"
  echo -e "\t List channels on 'peer0' of 'laboratory'".
  echo ""

  echo "fablo channel list laboratory peer1"
  echo -e "\t List channels on 'peer1' of 'laboratory'".
  echo ""

  echo "fablo channel list laboratory peer2"
  echo -e "\t List channels on 'peer2' of 'laboratory'".
  echo ""

  echo "fablo channel list insurance peer0"
  echo -e "\t List channels on 'peer0' of 'insurance'".
  echo ""

  echo "fablo channel list insurance peer1"
  echo -e "\t List channels on 'peer1' of 'insurance'".
  echo ""

  echo "fablo channel list insurance peer2"
  echo -e "\t List channels on 'peer2' of 'insurance'".
  echo ""

  echo "fablo channel getinfo healthcare-channel patient peer0"
  echo -e "\t Get channel info on 'peer0' of 'patient'".
  echo ""
  echo "fablo channel fetch config healthcare-channel patient peer0 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer0' of 'patient'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel patient peer0 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer0' of 'patient'".
  echo ""

  echo "fablo channel getinfo healthcare-channel patient peer1"
  echo -e "\t Get channel info on 'peer1' of 'patient'".
  echo ""
  echo "fablo channel fetch config healthcare-channel patient peer1 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer1' of 'patient'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel patient peer1 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer1' of 'patient'".
  echo ""

  echo "fablo channel getinfo healthcare-channel patient peer2"
  echo -e "\t Get channel info on 'peer2' of 'patient'".
  echo ""
  echo "fablo channel fetch config healthcare-channel patient peer2 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer2' of 'patient'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel patient peer2 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer2' of 'patient'".
  echo ""

  echo "fablo channel getinfo healthcare-channel doctor peer0"
  echo -e "\t Get channel info on 'peer0' of 'doctor'".
  echo ""
  echo "fablo channel fetch config healthcare-channel doctor peer0 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer0' of 'doctor'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel doctor peer0 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer0' of 'doctor'".
  echo ""

  echo "fablo channel getinfo healthcare-channel doctor peer1"
  echo -e "\t Get channel info on 'peer1' of 'doctor'".
  echo ""
  echo "fablo channel fetch config healthcare-channel doctor peer1 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer1' of 'doctor'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel doctor peer1 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer1' of 'doctor'".
  echo ""

  echo "fablo channel getinfo healthcare-channel doctor peer2"
  echo -e "\t Get channel info on 'peer2' of 'doctor'".
  echo ""
  echo "fablo channel fetch config healthcare-channel doctor peer2 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer2' of 'doctor'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel doctor peer2 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer2' of 'doctor'".
  echo ""

  echo "fablo channel getinfo healthcare-channel pharmacy peer0"
  echo -e "\t Get channel info on 'peer0' of 'pharmacy'".
  echo ""
  echo "fablo channel fetch config healthcare-channel pharmacy peer0 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer0' of 'pharmacy'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel pharmacy peer0 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer0' of 'pharmacy'".
  echo ""

  echo "fablo channel getinfo healthcare-channel pharmacy peer1"
  echo -e "\t Get channel info on 'peer1' of 'pharmacy'".
  echo ""
  echo "fablo channel fetch config healthcare-channel pharmacy peer1 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer1' of 'pharmacy'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel pharmacy peer1 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer1' of 'pharmacy'".
  echo ""

  echo "fablo channel getinfo healthcare-channel pharmacy peer2"
  echo -e "\t Get channel info on 'peer2' of 'pharmacy'".
  echo ""
  echo "fablo channel fetch config healthcare-channel pharmacy peer2 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer2' of 'pharmacy'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel pharmacy peer2 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer2' of 'pharmacy'".
  echo ""

  echo "fablo channel getinfo healthcare-channel laboratory peer0"
  echo -e "\t Get channel info on 'peer0' of 'laboratory'".
  echo ""
  echo "fablo channel fetch config healthcare-channel laboratory peer0 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer0' of 'laboratory'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel laboratory peer0 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer0' of 'laboratory'".
  echo ""

  echo "fablo channel getinfo healthcare-channel laboratory peer1"
  echo -e "\t Get channel info on 'peer1' of 'laboratory'".
  echo ""
  echo "fablo channel fetch config healthcare-channel laboratory peer1 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer1' of 'laboratory'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel laboratory peer1 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer1' of 'laboratory'".
  echo ""

  echo "fablo channel getinfo healthcare-channel laboratory peer2"
  echo -e "\t Get channel info on 'peer2' of 'laboratory'".
  echo ""
  echo "fablo channel fetch config healthcare-channel laboratory peer2 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer2' of 'laboratory'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel laboratory peer2 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer2' of 'laboratory'".
  echo ""

  echo "fablo channel getinfo healthcare-channel insurance peer0"
  echo -e "\t Get channel info on 'peer0' of 'insurance'".
  echo ""
  echo "fablo channel fetch config healthcare-channel insurance peer0 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer0' of 'insurance'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel insurance peer0 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer0' of 'insurance'".
  echo ""

  echo "fablo channel getinfo healthcare-channel insurance peer1"
  echo -e "\t Get channel info on 'peer1' of 'insurance'".
  echo ""
  echo "fablo channel fetch config healthcare-channel insurance peer1 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer1' of 'insurance'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel insurance peer1 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer1' of 'insurance'".
  echo ""

  echo "fablo channel getinfo healthcare-channel insurance peer2"
  echo -e "\t Get channel info on 'peer2' of 'insurance'".
  echo ""
  echo "fablo channel fetch config healthcare-channel insurance peer2 [file-name.json]"
  echo -e "\t Download latest config block and save it. Uses first peer 'peer2' of 'insurance'".
  echo ""
  echo "fablo channel fetch <newest|oldest|block-number> healthcare-channel insurance peer2 [file name]"
  echo -e "\t Fetch a block with given number and save it. Uses first peer 'peer2' of 'insurance'".
  echo ""

}
