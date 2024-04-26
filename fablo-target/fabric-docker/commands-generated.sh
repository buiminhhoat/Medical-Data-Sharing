#!/usr/bin/env bash

generateArtifacts() {
  printHeadline "Generating basic configs" "U1F913"

  printItalics "Generating crypto material for Orderer" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-orderer.yaml" "peerOrganizations/orderer.healthcare.com" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating crypto material for patient" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-patient.yaml" "peerOrganizations/patient.healthcare.com" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating crypto material for doctor" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-doctor.yaml" "peerOrganizations/doctor.healthcare.com" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating crypto material for pharmacy" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-pharmacy.yaml" "peerOrganizations/pharmacy.healthcare.com" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating crypto material for laboratory" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-laboratory.yaml" "peerOrganizations/laboratory.healthcare.com" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating crypto material for insurance" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-insurance.yaml" "peerOrganizations/insurance.healthcare.com" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating genesis block for group orderers-group" "U1F3E0"
  genesisBlockCreate "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config" "Orderers-groupGenesis"

  # Create directory for chaincode packages to avoid permission errors on linux
  mkdir -p "$FABLO_NETWORK_ROOT/fabric-config/chaincode-packages"
}

startNetwork() {
  printHeadline "Starting network" "U1F680"
  (cd "$FABLO_NETWORK_ROOT"/fabric-docker && docker-compose up -d)
  sleep 4
}

generateChannelsArtifacts() {
  printHeadline "Generating config for 'healthcare-channel'" "U1F913"
  createChannelTx "healthcare-channel" "$FABLO_NETWORK_ROOT/fabric-config" "HealthcareChannel" "$FABLO_NETWORK_ROOT/fabric-config/config"
}

installChannels() {
  printHeadline "Creating 'healthcare-channel' on patient/peer0" "U1F63B"
  docker exec -i cli.patient.healthcare.com bash -c "source scripts/channel_fns.sh; createChannelAndJoinTls 'healthcare-channel' 'patientMSP' 'peer0.patient.healthcare.com:7041' 'crypto/users/Admin@patient.healthcare.com/msp' 'crypto/users/Admin@patient.healthcare.com/tls' 'crypto-orderer/tlsca.orderer.healthcare.com-cert.pem' 'orderer0.orderers-group.orderer.healthcare.com:7030';"

  printItalics "Joining 'healthcare-channel' on  doctor/peer0" "U1F638"
  docker exec -i cli.doctor.healthcare.com bash -c "source scripts/channel_fns.sh; fetchChannelAndJoinTls 'healthcare-channel' 'doctorMSP' 'peer0.doctor.healthcare.com:7061' 'crypto/users/Admin@doctor.healthcare.com/msp' 'crypto/users/Admin@doctor.healthcare.com/tls' 'crypto-orderer/tlsca.orderer.healthcare.com-cert.pem' 'orderer0.orderers-group.orderer.healthcare.com:7030';"
  printItalics "Joining 'healthcare-channel' on  pharmacy/peer0" "U1F638"
  docker exec -i cli.pharmacy.healthcare.com bash -c "source scripts/channel_fns.sh; fetchChannelAndJoinTls 'healthcare-channel' 'pharmacyMSP' 'peer0.pharmacy.healthcare.com:7081' 'crypto/users/Admin@pharmacy.healthcare.com/msp' 'crypto/users/Admin@pharmacy.healthcare.com/tls' 'crypto-orderer/tlsca.orderer.healthcare.com-cert.pem' 'orderer0.orderers-group.orderer.healthcare.com:7030';"
  printItalics "Joining 'healthcare-channel' on  laboratory/peer0" "U1F638"
  docker exec -i cli.laboratory.healthcare.com bash -c "source scripts/channel_fns.sh; fetchChannelAndJoinTls 'healthcare-channel' 'laboratoryMSP' 'peer0.laboratory.healthcare.com:7101' 'crypto/users/Admin@laboratory.healthcare.com/msp' 'crypto/users/Admin@laboratory.healthcare.com/tls' 'crypto-orderer/tlsca.orderer.healthcare.com-cert.pem' 'orderer0.orderers-group.orderer.healthcare.com:7030';"
  printItalics "Joining 'healthcare-channel' on  insurance/peer0" "U1F638"
  docker exec -i cli.insurance.healthcare.com bash -c "source scripts/channel_fns.sh; fetchChannelAndJoinTls 'healthcare-channel' 'insuranceMSP' 'peer0.insurance.healthcare.com:7121' 'crypto/users/Admin@insurance.healthcare.com/msp' 'crypto/users/Admin@insurance.healthcare.com/tls' 'crypto-orderer/tlsca.orderer.healthcare.com-cert.pem' 'orderer0.orderers-group.orderer.healthcare.com:7030';"
}

installChaincodes() {
  if [ -n "$(ls "$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java")" ]; then
    local version="0.0.1"
    printHeadline "Packaging chaincode 'chaincode1'" "U1F60E"
    chaincodeBuild "chaincode1" "java" "$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java" "16"
    chaincodePackage "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "chaincode1" "$version" "java" printHeadline "Installing 'chaincode1' for patient" "U1F60E"
    chaincodeInstall "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
    chaincodeApprove "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
    printHeadline "Installing 'chaincode1' for doctor" "U1F60E"
    chaincodeInstall "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
    chaincodeApprove "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
    printHeadline "Installing 'chaincode1' for pharmacy" "U1F60E"
    chaincodeInstall "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
    chaincodeApprove "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
    printHeadline "Installing 'chaincode1' for laboratory" "U1F60E"
    chaincodeInstall "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
    chaincodeApprove "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
    printHeadline "Installing 'chaincode1' for insurance" "U1F60E"
    chaincodeInstall "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
    chaincodeApprove "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
    printItalics "Committing chaincode 'chaincode1' on channel 'healthcare-channel' as 'patient'" "U1F618"
    chaincodeCommit "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "peer0.patient.healthcare.com:7041,peer0.doctor.healthcare.com:7061,peer0.pharmacy.healthcare.com:7081,peer0.laboratory.healthcare.com:7101,peer0.insurance.healthcare.com:7121" "crypto-peer/peer0.patient.healthcare.com/tls/ca.crt,crypto-peer/peer0.doctor.healthcare.com/tls/ca.crt,crypto-peer/peer0.pharmacy.healthcare.com/tls/ca.crt,crypto-peer/peer0.laboratory.healthcare.com/tls/ca.crt,crypto-peer/peer0.insurance.healthcare.com/tls/ca.crt" ""
  else
    echo "Warning! Skipping chaincode 'chaincode1' installation. Chaincode directory is empty."
    echo "Looked in dir: '$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java'"
  fi

}

installChaincode() {
  local chaincodeName="$1"
  if [ -z "$chaincodeName" ]; then
    echo "Error: chaincode name is not provided"
    exit 1
  fi

  local version="$2"
  if [ -z "$version" ]; then
    echo "Error: chaincode version is not provided"
    exit 1
  fi

  if [ "$chaincodeName" = "chaincode1" ]; then
    if [ -n "$(ls "$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java")" ]; then
      printHeadline "Packaging chaincode 'chaincode1'" "U1F60E"
      chaincodeBuild "chaincode1" "java" "$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java" "16"
      chaincodePackage "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "chaincode1" "$version" "java" printHeadline "Installing 'chaincode1' for patient" "U1F60E"
      chaincodeInstall "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printHeadline "Installing 'chaincode1' for doctor" "U1F60E"
      chaincodeInstall "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printHeadline "Installing 'chaincode1' for pharmacy" "U1F60E"
      chaincodeInstall "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printHeadline "Installing 'chaincode1' for laboratory" "U1F60E"
      chaincodeInstall "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printHeadline "Installing 'chaincode1' for insurance" "U1F60E"
      chaincodeInstall "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printItalics "Committing chaincode 'chaincode1' on channel 'healthcare-channel' as 'patient'" "U1F618"
      chaincodeCommit "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "peer0.patient.healthcare.com:7041,peer0.doctor.healthcare.com:7061,peer0.pharmacy.healthcare.com:7081,peer0.laboratory.healthcare.com:7101,peer0.insurance.healthcare.com:7121" "crypto-peer/peer0.patient.healthcare.com/tls/ca.crt,crypto-peer/peer0.doctor.healthcare.com/tls/ca.crt,crypto-peer/peer0.pharmacy.healthcare.com/tls/ca.crt,crypto-peer/peer0.laboratory.healthcare.com/tls/ca.crt,crypto-peer/peer0.insurance.healthcare.com/tls/ca.crt" ""

    else
      echo "Warning! Skipping chaincode 'chaincode1' install. Chaincode directory is empty."
      echo "Looked in dir: '$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java'"
    fi
  fi
}

runDevModeChaincode() {
  local chaincodeName=$1
  if [ -z "$chaincodeName" ]; then
    echo "Error: chaincode name is not provided"
    exit 1
  fi

  if [ "$chaincodeName" = "chaincode1" ]; then
    local version="0.0.1"
    printHeadline "Approving 'chaincode1' for patient (dev mode)" "U1F60E"
    chaincodeApprove "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "healthcare-channel" "chaincode1" "0.0.1" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "" ""
    printHeadline "Approving 'chaincode1' for doctor (dev mode)" "U1F60E"
    chaincodeApprove "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "healthcare-channel" "chaincode1" "0.0.1" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "" ""
    printHeadline "Approving 'chaincode1' for pharmacy (dev mode)" "U1F60E"
    chaincodeApprove "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "healthcare-channel" "chaincode1" "0.0.1" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "" ""
    printHeadline "Approving 'chaincode1' for laboratory (dev mode)" "U1F60E"
    chaincodeApprove "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "healthcare-channel" "chaincode1" "0.0.1" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "" ""
    printHeadline "Approving 'chaincode1' for insurance (dev mode)" "U1F60E"
    chaincodeApprove "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "healthcare-channel" "chaincode1" "0.0.1" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "" ""
    printItalics "Committing chaincode 'chaincode1' on channel 'healthcare-channel' as 'patient' (dev mode)" "U1F618"
    chaincodeCommit "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "healthcare-channel" "chaincode1" "0.0.1" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "" "peer0.patient.healthcare.com:7041,peer0.doctor.healthcare.com:7061,peer0.pharmacy.healthcare.com:7081,peer0.laboratory.healthcare.com:7101,peer0.insurance.healthcare.com:7121" "" ""

  fi
}

upgradeChaincode() {
  local chaincodeName="$1"
  if [ -z "$chaincodeName" ]; then
    echo "Error: chaincode name is not provided"
    exit 1
  fi

  local version="$2"
  if [ -z "$version" ]; then
    echo "Error: chaincode version is not provided"
    exit 1
  fi

  if [ "$chaincodeName" = "chaincode1" ]; then
    if [ -n "$(ls "$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java")" ]; then
      printHeadline "Packaging chaincode 'chaincode1'" "U1F60E"
      chaincodeBuild "chaincode1" "java" "$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java" "16"
      chaincodePackage "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "chaincode1" "$version" "java" printHeadline "Installing 'chaincode1' for patient" "U1F60E"
      chaincodeInstall "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printHeadline "Installing 'chaincode1' for doctor" "U1F60E"
      chaincodeInstall "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com:7061" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printHeadline "Installing 'chaincode1' for pharmacy" "U1F60E"
      chaincodeInstall "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com:7081" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printHeadline "Installing 'chaincode1' for laboratory" "U1F60E"
      chaincodeInstall "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com:7101" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printHeadline "Installing 'chaincode1' for insurance" "U1F60E"
      chaincodeInstall "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
      chaincodeApprove "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com:7121" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" ""
      printItalics "Committing chaincode 'chaincode1' on channel 'healthcare-channel' as 'patient'" "U1F618"
      chaincodeCommit "cli.patient.healthcare.com" "peer0.patient.healthcare.com:7041" "healthcare-channel" "chaincode1" "$version" "orderer0.orderers-group.orderer.healthcare.com:7030" "" "false" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem" "peer0.patient.healthcare.com:7041,peer0.doctor.healthcare.com:7061,peer0.pharmacy.healthcare.com:7081,peer0.laboratory.healthcare.com:7101,peer0.insurance.healthcare.com:7121" "crypto-peer/peer0.patient.healthcare.com/tls/ca.crt,crypto-peer/peer0.doctor.healthcare.com/tls/ca.crt,crypto-peer/peer0.pharmacy.healthcare.com/tls/ca.crt,crypto-peer/peer0.laboratory.healthcare.com/tls/ca.crt,crypto-peer/peer0.insurance.healthcare.com/tls/ca.crt" ""

    else
      echo "Warning! Skipping chaincode 'chaincode1' upgrade. Chaincode directory is empty."
      echo "Looked in dir: '$CHAINCODES_BASE_DIR/./chaincodes/chaincode-java'"
    fi
  fi
}

notifyOrgsAboutChannels() {
  printHeadline "Creating new channel config blocks" "U1F537"
  createNewChannelUpdateTx "healthcare-channel" "patientMSP" "HealthcareChannel" "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config"
  createNewChannelUpdateTx "healthcare-channel" "doctorMSP" "HealthcareChannel" "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config"
  createNewChannelUpdateTx "healthcare-channel" "pharmacyMSP" "HealthcareChannel" "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config"
  createNewChannelUpdateTx "healthcare-channel" "laboratoryMSP" "HealthcareChannel" "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config"
  createNewChannelUpdateTx "healthcare-channel" "insuranceMSP" "HealthcareChannel" "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config"

  printHeadline "Notyfing orgs about channels" "U1F4E2"
  notifyOrgAboutNewChannelTls "healthcare-channel" "patientMSP" "cli.patient.healthcare.com" "peer0.patient.healthcare.com" "orderer0.orderers-group.orderer.healthcare.com:7030" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
  notifyOrgAboutNewChannelTls "healthcare-channel" "doctorMSP" "cli.doctor.healthcare.com" "peer0.doctor.healthcare.com" "orderer0.orderers-group.orderer.healthcare.com:7030" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
  notifyOrgAboutNewChannelTls "healthcare-channel" "pharmacyMSP" "cli.pharmacy.healthcare.com" "peer0.pharmacy.healthcare.com" "orderer0.orderers-group.orderer.healthcare.com:7030" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
  notifyOrgAboutNewChannelTls "healthcare-channel" "laboratoryMSP" "cli.laboratory.healthcare.com" "peer0.laboratory.healthcare.com" "orderer0.orderers-group.orderer.healthcare.com:7030" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"
  notifyOrgAboutNewChannelTls "healthcare-channel" "insuranceMSP" "cli.insurance.healthcare.com" "peer0.insurance.healthcare.com" "orderer0.orderers-group.orderer.healthcare.com:7030" "crypto-orderer/tlsca.orderer.healthcare.com-cert.pem"

  printHeadline "Deleting new channel config blocks" "U1F52A"
  deleteNewChannelUpdateTx "healthcare-channel" "patientMSP" "cli.patient.healthcare.com"
  deleteNewChannelUpdateTx "healthcare-channel" "doctorMSP" "cli.doctor.healthcare.com"
  deleteNewChannelUpdateTx "healthcare-channel" "pharmacyMSP" "cli.pharmacy.healthcare.com"
  deleteNewChannelUpdateTx "healthcare-channel" "laboratoryMSP" "cli.laboratory.healthcare.com"
  deleteNewChannelUpdateTx "healthcare-channel" "insuranceMSP" "cli.insurance.healthcare.com"
}

printStartSuccessInfo() {
  printHeadline "Done! Enjoy your fresh network" "U1F984"
}

stopNetwork() {
  printHeadline "Stopping network" "U1F68F"
  (cd "$FABLO_NETWORK_ROOT"/fabric-docker && docker-compose stop)
  sleep 4
}

networkDown() {
  printHeadline "Destroying network" "U1F916"
  (cd "$FABLO_NETWORK_ROOT"/fabric-docker && docker-compose down)

  printf "Removing chaincode containers & images... \U1F5D1 \n"
  for container in $(docker ps -a | grep "dev-peer0.patient.healthcare.com-chaincode1" | awk '{print $1}'); do
    echo "Removing container $container..."
    docker rm -f "$container" || echo "docker rm of $container failed. Check if all fabric dockers properly was deleted"
  done
  for image in $(docker images "dev-peer0.patient.healthcare.com-chaincode1*" -q); do
    echo "Removing image $image..."
    docker rmi "$image" || echo "docker rmi of $image failed. Check if all fabric dockers properly was deleted"
  done
  for container in $(docker ps -a | grep "dev-peer0.doctor.healthcare.com-chaincode1" | awk '{print $1}'); do
    echo "Removing container $container..."
    docker rm -f "$container" || echo "docker rm of $container failed. Check if all fabric dockers properly was deleted"
  done
  for image in $(docker images "dev-peer0.doctor.healthcare.com-chaincode1*" -q); do
    echo "Removing image $image..."
    docker rmi "$image" || echo "docker rmi of $image failed. Check if all fabric dockers properly was deleted"
  done
  for container in $(docker ps -a | grep "dev-peer0.pharmacy.healthcare.com-chaincode1" | awk '{print $1}'); do
    echo "Removing container $container..."
    docker rm -f "$container" || echo "docker rm of $container failed. Check if all fabric dockers properly was deleted"
  done
  for image in $(docker images "dev-peer0.pharmacy.healthcare.com-chaincode1*" -q); do
    echo "Removing image $image..."
    docker rmi "$image" || echo "docker rmi of $image failed. Check if all fabric dockers properly was deleted"
  done
  for container in $(docker ps -a | grep "dev-peer0.laboratory.healthcare.com-chaincode1" | awk '{print $1}'); do
    echo "Removing container $container..."
    docker rm -f "$container" || echo "docker rm of $container failed. Check if all fabric dockers properly was deleted"
  done
  for image in $(docker images "dev-peer0.laboratory.healthcare.com-chaincode1*" -q); do
    echo "Removing image $image..."
    docker rmi "$image" || echo "docker rmi of $image failed. Check if all fabric dockers properly was deleted"
  done
  for container in $(docker ps -a | grep "dev-peer0.insurance.healthcare.com-chaincode1" | awk '{print $1}'); do
    echo "Removing container $container..."
    docker rm -f "$container" || echo "docker rm of $container failed. Check if all fabric dockers properly was deleted"
  done
  for image in $(docker images "dev-peer0.insurance.healthcare.com-chaincode1*" -q); do
    echo "Removing image $image..."
    docker rmi "$image" || echo "docker rmi of $image failed. Check if all fabric dockers properly was deleted"
  done

  printf "Removing generated configs... \U1F5D1 \n"
  rm -rf "$FABLO_NETWORK_ROOT/fabric-config/config"
  rm -rf "$FABLO_NETWORK_ROOT/fabric-config/crypto-config"
  rm -rf "$FABLO_NETWORK_ROOT/fabric-config/chaincode-packages"

  printHeadline "Done! Network was purged" "U1F5D1"
}
