/////////////////////
//if present put client data to fields
////////////////////
function fillClientDataPlus(){
  var decodedUrl = decodeURI(window.location.href);
  var queryParts = decodedUrl.split('?'); 
  if (queryParts.length != 2) {
	  alert("Error! No id for client! Impossible to continue");
	  return;
  }
  try{
    var theResult = JSON.parse(queryParts[1]);
  }
  catch(exception){
    alert("Server error! Reply is not in JSON format!");
    return;
  }
  window.querySaved = queryParts[1];
  if (!theResult['id']){
    alert("Error! No id for client! Impossible to continue");
    return;
  }
  window.savedClientInfo = theResult;
  if (theResult["surname"]){
	  surname_input = document.getElementById('surname_input');
	  if (surname_input){
		  surname_input.value=theResult["surname"];
	  }
  }
  if (theResult["name"]){
	  name_input = document.getElementById('name_input');
	  if (name_input){
		  name_input.value=theResult["name"];
	  }
  }
  if (theResult["patr"]){
	  patr_input = document.getElementById('patr_input');
	  if (patr_input){
		  patr_input.value=theResult["patr"];
	  }	  
  }
  if (theResult['DoB']){
	  var date_input = document.getElementById('date_input');
	  if (date_input){
		  date_input.valueAsDate=new Date(Number(theResult['DoB']));
	  }
  }
  if (theResult['passSeries']){
	  var passSeries_clientInput = document.getElementById('pass_serial_input');
	  if (passSeries_clientInput){
		  passSeries_clientInput.value=theResult['passSeries'];
	  }
  }
  if (theResult['passNum']){
	  var passNum_clientInput = document.getElementById('pass_num_input');
	  if (passNum_clientInput){
		  passNum_clientInput.value=theResult['passNum'];
	  }
  }
}
fillClientDataPlus();

var cancel_btn = document.getElementById("cancel_btn");
if (cancel_btn){
	cancel_btn.onclick = function() {
		if (window.querySaved){
			window.location.href = "./CreateContract.html?"+encodeURI(querySaved);	
		}
		else {
			window.location.href = "./CreateContract.html";
		}
	}
}

//{ "surname":"...", "name":"...", "patr":"...", "DoB":long, "passSeries":"...", "passNum":"..."} 
var save_btn = document.getElementById("save_btn");
if (save_btn){
	  save_btn.onclick = function() {		  
		  var argument = {};		  
		  
		  ////////////////////////////Checking variables START/////////////////////////////////////
		  var surnameElem = document.getElementById("surname_input");
		  if (!surnameElem || !surnameElem.value) {
			  alert("Необходимо указать фамилию")
			  return;
		  }
		  argument["surname"] = surnameElem.value;
		  
		  var nameElem = document.getElementById("name_input");
		  if (!nameElem || !nameElem.value){
			  alert("Необходимо указать имя")
			  return;
		  }
		  argument["name"]=nameElem.value;
		  
		  var patrElem = document.getElementById("patr_input");
		  if (patrElem && patrElem.value && patrElem.value.length>0){
			  argument["patr"]=patrElem.value;
		  }
		  
		  //Don't forget that it is still a string!
		  var dateElem = document.getElementById("date_input");
		  if (!dateElem || !dateElem.value){
			  alert("Необходимо указать дату рождения")
			  return;
		  }
		  var dob = new Date(dateElem.value);
		  argument["DoB"]=dob.getTime();
		  
		  var pass_serial_Elem = document.getElementById("pass_serial_input");
		  if (!pass_serial_Elem || !pass_serial_Elem.value){
			  alert("Необходимо ввести серию")
			  return;
		  }
		  if (pass_serial_Elem.value.length!=4){
			  alert("Серия должна быть ровно 4 цифры")
			  return;
		  }
		  if (! /^\d+$/.test(pass_serial_Elem.value)){
			  alert("Серия должна состоять только из цифр")
			  return;
		  }
		  argument["passSeries"]=pass_serial_Elem.value;
		  
		  var pass_num_Elem = document.getElementById("pass_num_input");
		  if (!pass_num_Elem || !pass_num_Elem.value){
			  alert("Необходимо ввести серию")
			  return;
		  }
		  if (pass_num_Elem.value.length!=6){
			  alert("Номер должен быть ровно 6 цифр")
			  return;
		  }
		  if (! /^\d+$/.test(pass_num_Elem.value)){
			  alert("Номер должен состоять только из цифр")
			  return;
		  }
		  argument["passNum"]=pass_num_Elem.value;
		  ////////////////////////////Checking variables END/////////////////////////////////////
		  
		  argument['id'] = window.savedClientInfo['id'];
		  
		  ///////////////////////////Request sending START////////////////////////////////////////////////
		  var request = new XMLHttpRequest();
		  //Use next 2 in case of async
		  //request.timeout = 30000;
		  //request.ontimeout = function() {alert("Connection timed out!");}
		  request.open("POST", "./api/changeClient", false);//NOT ASYNC HERE!
		  request.send(encodeURI(JSON.stringify(argument)))		  
		  ///////////////////////////Request sending END////////////////////////////////////////////////
		  
		  ///////////////////////////Result parsing START/////////////////////////////////////////
		  if (request.status!=200){
			  alert('Error! Status='+request.status+"Text:"+request.responseText);
			  return;
		  }
		  var theReply=request.responseText;
		  if (!theReply){
			  alert('Server error! Reply is null!');
			  return;
		  }
		  var theResult;
		  try{
			  theResult = JSON.parse(theReply);
		  }
		  catch(exception){
			  alert("Server error! Reply is not in JSON format!");
			  return;
		  }
		  if (!theResult['error']){
			  alert("Server error! Expected option not found!");
			  return;
		  }
		  if (theResult['error'] !== -1){
			  alert("Сохранение не удалось. Ошибка:"+theResult['error']);
			  return;
		  }
		  ////////////////////////////Result parsing END/////////////////////////////////////////////
		  argument['FIO']=argument["surname"]+' '+argument["name"];
		  if (argument["patr"]) {
			  argument['FIO']=argument['FIO']+' '+argument["patr"];
		  }
		  var jsonToPush = JSON.stringify(argument);
		  window.location.href = "./CreateContract.html?"+encodeURI(jsonToPush); 
	  }
}
