//this.sender表示发送者，是一个Entity
function testFlag() {
  addFlag({
    entity: this.sender,
    key: "test"
  })
}
function a() {
  removeFlag(this.sender, "test")
}

function b() {
  print(getFlagEntities("test"))
}