pragma solidity ^0.8.0;

contract BailRural {

  struct LandInformation {
    string owner;
    uint[] gpsPoints;
  }

  mapping (address => LandInformation) lands;

  event LandRecorded(
    address indexed _address,
    string _owner,
    uint[] _gpsPoints
  );

  function recordLand(string memory _owner, uint[] memory _gpsPoints) public {
    lands[msg.sender].owner = _owner;
    lands[msg.sender].gpsPoints = _gpsPoints;
    emit LandRecorded(msg.sender, _owner , _gpsPoints);
  }

  function getLandInformation(address _address) public view returns (string memory, uint[] memory) {
    return (lands[_address].owner,  lands[_address].gpsPoints);
  }

}
