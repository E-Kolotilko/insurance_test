var cancel_btn = document.getElementById("cancel_btn");
if (cancel_btn){
	cancel_btn.onclick = function() {
		window.location.href = "./CreateContract.html";
	  }
}
var create_btn = document.getElementById("new_btn");
if (create_btn){
	  create_btn.onclick = function() {
		window.location.href = "./CreateClient.html";
	  }
}

var select_btn=document.getElementById("select_btn");
if (select_btn){
	select_btn.onclick = function() {
		if (theTableIndex<0 || (clients_array.length == 0)) {
		    alert('Не выбран ни один вариант!')
		}
		var client = clients_array[theTableIndex-1];
		client['FIO']=client["surname"]+' '+client["name"];
		if (client["patr"]) {
			client['FIO']=client['FIO']+' '+client["patr"];
		}
		var jsonToPush = JSON.stringify(client);
	    window.location.href = "./CreateContract.html?"+encodeURI(jsonToPush); 
  }
}

var theTableIndex=-1;

function selectGlobalIndex(){
	theTableIndex = this.rowIndex;
}

var image_btn = document.getElementById("img_btn");
if (image_btn){
	  image_btn.onclick = function() {
		  var argument = {};
		  
		  var surnameElem = document.getElementById("surname_input");
		  if (surnameElem && surnameElem.value && surnameElem.value.length>0) {
			  argument["surname"] = surnameElem.value;		  
		  }
		  
		  var nameElem = document.getElementById("name_input");
		  if (nameElem && nameElem.value && nameElem.value.length>0){
			  argument["name"]=nameElem.value;
		  }
		  
		  var patrElem = document.getElementById("patr_input");
		  if (patrElem && patrElem.value && patrElem.value.length>0){
			  argument["patr"]=patrElem.value;
		  }
		  
		  var argumentEncoded = encodeURI(JSON.stringify(argument));
		  var request = new XMLHttpRequest();
		  request.open("GET", "./api/lookForClient?"+argumentEncoded, false);//NOT ASYNC HERE!
		  request.send();
		  
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
		  if (!theResult['clients']){
			  alert("Сервер прислал ответ в неверном формате");
			  return;
		  }
		  window.clients_array = theResult['clients'];
		  var theTable = document.getElementById("client_table");
		  theTable.deleteTFoot();
		  var theTable_tFoot = document.createElement("tfoot");
		  for (var i=0; i<theResult['clients'].length; i++){
			  var row = document.createElement('tr');
			  row.addEventListener("click",selectGlobalIndex);
			  var client = theResult['clients'][i];

			  var fioTd = document.createElement('td');
			  var FIO = client['surname']+' '+client['name'];
			  if (client['patr']){
				  FIO+=' '+client['patr'];
			  }
			  fioTd.innerText = FIO;
			  row.appendChild(fioTd);
			  
			  var DoBTd = document.createElement('td');
			  DoBTd.innerText=( new Date(Number(client['DoB'])) ).toLocaleDateString();
			  row.appendChild(DoBTd);
			  
			  var passpDataTd = document.createElement('td');
			  var passData;
			  if (theResult['passSeries']){
				  passData = theResult['passSeries'];
			  }
			  if (theResult['passNum']){
				  passData = passData ? passData+' ' + theResult['passNum'] : theResult['passNum']  
			  }
			  if (passData){
				  passpDataTd.innerText=passData;  
			  }
			  else {
				  passpDataTd.innerText='';
			  }
			  row.appendChild(passpDataTd);
			  
			  theTable_tFoot.appendChild(row);
		  }
		  theTable.appendChild(theTable_tFoot);
	  }
}
