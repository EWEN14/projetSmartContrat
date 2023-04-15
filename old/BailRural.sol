// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract BailRural {

  struct LandInformation {
    string owner;
    uint area;
    uint[] gpsPoints;
  }

  mapping (address => LandInformation) lands;

  event LandRecorded(
    address indexed _address,
    string _owner,
    uint _area,
    uint[] _gpsPoints
  );

  function recordLand(string memory _owner, uint _area, uint[] memory _gpsPoints) public {
    lands[msg.sender].owner = _owner;
    lands[msg.sender].area = _area;
    lands[msg.sender].gpsPoints = _gpsPoints;
    emit LandRecorded(msg.sender, _owner, _area, _gpsPoints);
  }

  function getLandInformation(address _address) public view returns (string memory, uint, uint[] memory) {
    return (lands[_address].owner, lands[_address].area, lands[_address].gpsPoints);
  }

}
