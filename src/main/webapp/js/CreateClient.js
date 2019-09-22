
var cancel_btn = document.getElementById("cancel_btn");
if (cancel_btn){
	cancel_btn.onclick = function() {
		window.location.href = "./SelectClient.html";
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
		  ////////////////////////////Checking variables END/////////////////////////////////////
		  
		  ///////////////////////////Request sending START////////////////////////////////////////////////
		  var request = new XMLHttpRequest();
		  //Use next 2 in case of async
		  //request.timeout = 30000;
		  //request.ontimeout = function() {alert("Connection timed out!");}
		  request.open("POST", "./api/createClient", false);//NOT ASYNC HERE!
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
		  if (theResult['id']){
			  argument['id']=theResult['id'];
			  argument['FIO']=argument["surname"]+' '+argument["name"];
			  if (argument["patr"]) {
				  argument['FIO']=argument['FIO']+' '+argument["patr"];
			  }
			  var jsonToPush = JSON.stringify(argument);
			  window.location.href = "./CreateContract.html?"+encodeURI(jsonToPush);  
		  }
	  }
}
