var contractDate_contractInput = document.getElementById('contractDate_contractInput');
if (contractDate_contractInput){
	contractDate_contractInput.valueAsDate = new Date();
}
/////////////////////
//if present put client data to fields
////////////////////
function fillClientDataPlus(){
  var decodedUrl = decodeURI(window.location.href);
  var queryParts = decodedUrl.split('?'); 
  if (queryParts.length != 2) return;
  try{
    var theResult = JSON.parse(queryParts[1]);
  }
  catch(exception){
    alert("Server error! Reply is not in JSON format!");
    return;
  }
  if (!theResult['id']){
    alert("Error! No id for client! No info will be filled");
    return;
  }
  window.savedClientInfo = theResult;
  if (theResult['FIO']){
	  var clientFIO_clientInput = document.getElementById('clientFIO_clientInput');
	  if (clientFIO_clientInput){
		  clientFIO_clientInput.value=theResult['FIO'];
	  }
  }
  if (theResult['DoB']){
	  var DoB_clientInput = document.getElementById('DoB_clientInput');
	  if (DoB_clientInput){
		  DoB_clientInput.valueAsDate=new Date(Number(theResult['DoB']));
	  }
  }
  if (theResult['passSeries']){
	  var passSeries_clientInput = document.getElementById('passSeries_clientInput');
	  if (passSeries_clientInput){
		  passSeries_clientInput.value=theResult['passSeries'];
	  }
  }
  if (theResult['passNum']){
	  var passNum_clientInput = document.getElementById('passNum_clientInput');
	  if (passNum_clientInput){
		  passNum_clientInput.value=theResult['passNum'];
	  }
  }
}
fillClientDataPlus();



var calculate_btn  = document.getElementById("calculate_btn"); 
if (calculate_btn){
	calculate_btn.onclick = function() {
		/*
	* in: { "insuranceAmount":..., "days":..., "propertyType":"...", "builtYear":..., "area":... }
     * out: {"error":-1, "award":... } if OK or {"error":"..." } 
		 */
		argument={};
		var insuranceAmount_contractInput=document.getElementById('insuranceAmount_contractInput'); 
		if (!insuranceAmount_contractInput || !insuranceAmount_contractInput.value){
			alert('Не введена сумма');
			return;
		}
		argument['insuranceAmount']=insuranceAmount_contractInput.value;
		
		var daysFromElem=document.getElementById('dateFrom_contractInput');
		var daysToDateElem=document.getElementById('dateTo_contractInput');
		if (!daysFromElem || !daysFromElem.value){
			alert('Не выбрано начало срока');
			return;
		}
		if (!daysToDateElem || !daysToDateElem.value){
			alert('Не выбрано окончание срока');
			return;
		}
		if (daysToDateElem.valueAsNumber<daysFromElem.valueAsNumber){
			alert('Окончание срока раньше начала');
			return;
		}
		argument['days']=(daysToDateElem.valueAsNumber-daysFromElem.valueAsNumber)/(1000*60*60*24);
		
		var propertyType = document.getElementById('propertyType_contractInput');
		if (!propertyType || !propertyType.value){
			alert('Не выбран тип собственности');
			return;
		}
		argument['propertyType'] = propertyType.value;
		
		var year_contractInput =  document.getElementById('year_contractInput');
		if (!year_contractInput || !year_contractInput.value){
			alert('Не выбран год');
			return;
		}
		argument['builtYear'] = year_contractInput.value;
		
		var area_contractInput =  document.getElementById('area_contractInput');
		if (!area_contractInput || !area_contractInput.value){
			alert('Не выбрана площадь');
			return;
		}
		argument['area'] = area_contractInput.value;
		var argumentEncoded = encodeURI(JSON.stringify(argument));
		var request = new XMLHttpRequest();
		request.open("GET", "./api/calculateAward?"+argumentEncoded, false);//NOT ASYNC HERE!
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
			  alert("Расчёт не удался. Ошибка:"+theResult['error']);
			  return;
		  }
		  ////////////////////////////Result parsing END/////////////////////////////////////////////
		var award_contractInput = document.getElementById('award_contractInput');
		award_contractInput.value=theResult['award']>0 ? theResult['award'].toFixed(2) : 0;
		var awardCountDate_contractInput = document.getElementById('awardCountDate_contractInput'); 
		awardCountDate_contractInput.valueAsDate=new Date();
	}
}


var selectClient_btn  = document.getElementById("selectClient_btn"); 
if (selectClient_btn){
	selectClient_btn.onclick = function() {
		window.location.href = "./SelectClient.html";
	}
}


var changeClient_btn  = document.getElementById("changeClient_btn"); 
if (changeClient_btn){
	changeClient_btn.onclick = function() {
		if (window.savedClientInfo){
			  var jsonToPush = JSON.stringify(window.savedClientInfo);
			  window.location.href = "./ChangeClient.html?"+encodeURI(jsonToPush);  
		}
		else {
			window.location.href = "./ChangeClient.html";	
		}
	}
}

var backToList_btn = document.getElementById("backToList_btn");
if (backToList_btn){
	backToList_btn.onclick = function() {
		window.location.href = "./";
	}
}

var save_btn = document.getElementById("save_btn");
if (save_btn){
	save_btn.onclick = function() {
		var contract = {}
		var contractDataElements = document.getElementsByClassName("contractData")
		for (var i=0; i<contractDataElements.length; i++){
			if (contractDataElements[i].required && !contractDataElements[i].value){
				alert('Не указан параметр' + contractDataElements[i].name);
				return;
			}
			if (contractDataElements[i].type=="date"){
				contract[contractDataElements[i].name]=contractDataElements[i].valueAsNumber;
			} 
			else {
				contract[contractDataElements[i].name]=contractDataElements[i].value;				
			}
		}
		contract['clientId']=window.savedClientInfo['id'];
				
		//window.savedClientInfo
		var client = {};
		var clientDataElements = document.getElementsByClassName("clientData");
		for (var i=0; i<clientDataElements.length; i++){
			if (clientDataElements[i].required && !clientDataElements[i].value){
				alert('Не указан параметр' + clientDataElements[i].name);
				return;
			}
			if (clientDataElements[i].type=="date"){
				client[clientDataElements[i].name]=clientDataElements[i].valueAsNumber;
			} 
			else {
				client[clientDataElements[i].name]=clientDataElements[i].value;				
			}
		}
		if (!window.savedClientInfo['id']){
			alert("Не выбран id клиента (не был осуществлен выбор через имеющийся функционал?")
			return;
		}
		client['id']=window.savedClientInfo['id'];
		if (!window.savedClientInfo['surname'] || !window.savedClientInfo['name']) {
			alert("Не хватает частей ФИО (не был осуществлен выбор через имеющийся функционал?")
			return;
		}
		client['surname']=window.savedClientInfo['surname'];
		client['name']=window.savedClientInfo['name'];
		if (window.savedClientInfo['patr']){
			client['patr']=window.savedClientInfo['patr'];	
		}		
		
		var argument = {"client":client, "contract":contract};
		var argumentEncoded = encodeURI(JSON.stringify(argument));
		var request = new XMLHttpRequest();
		request.open("POST", "./api/createContract", false);//NOT ASYNC HERE!
		request.send(argumentEncoded);		  
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
			  alert("Добавление не удалось. Ошибка:"+theResult['error']);
			  return;
		  }
		  ////////////////////////////Result parsing END/////////////////////////////////////////////
		  window.location.href = "./";
	}
}
