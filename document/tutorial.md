# OCE IAM

## Tutorial

### Architecture

![](images/service-wrapper.png)

서비스 래퍼는 동일한 스프링 프레임워크 내의 2가지의 서블릿 구조를 가지고 있습니다.

하나는 / path 로 시작하는 Proxy Servlet 이고, 나머지 하나는 /service-console 로 시작하는 UI 컨트롤러 입니다.

서버를 구동 후 UI 에 접근하기 위해서는 /service-console/index 로 접근하도록 합니다.


#### Spec

 - tomcat8
 
 - java8
 
 - couchDB

#### Features

**개발된 기능들**

- Proxy Server

- Custom Handler registration

- URI Mapping using same way as the Servlet Mapping rule

- URI Mapping using Cash Memory

- Https Support

- Broken Pipe error handling

- Authentication processing using Cash Memory

- Authentication information embed in Custom script 

- UI Security

- Gateway Workflow compliance with the Bpmn 2.0 Standard Spec


**추후 추가될 기능들**

- Multiple 토큰 Location 지원

- Auto Refresh Token Flow 지원
 
- 성능 모니터링

- 트랜잭션 별 로그 및 추적 기능 처리


### Configuration

메뉴의 Configuration 에서 연동될 IAM 서버를 설정할 수 있습니다.

![](images/wrapper-conf.png)

 - Host : Iam 서버 호스트
 
 - Port : Iam 서버 포트
 
 - managementKey : Iam 에서 생성한 managementKey
 
 - managementSecret : Iam 에서 생성한 managementSecret

### Policy

Policy 는 프록시 서버 수행 규칙을 설정하는 메뉴입니다.

![](images/wrapper-policy.png)

 - Name : 규칙 이름
 
 - Check Authentication : 체크시 인증 여부를 검증합니다.
 
 - Location : Check Authentication 이 활성화 되 있을 경우 필수값. 토큰의 위치입니다.
 
 - Parameter Name : Check Authentication 이 활성화 되 있을 경우 필수값. 토큰의 파라미터 이름입니다.
 
 - Proxy Uri : 프락시 동작을 위한 타겟 호스트입니다.
 
 - Prefix Uri : 프락시 동작시 일부 Path Information 을 수정하기 위한 옵션입니다.
 
 - Before Use : 프락시 동작을 하기 전 실행될 스크립트입니다. true, false 에 따라 동작 여부를 결정합니다.
  Authentication 이 active 이면 Authentication 후에 이 스크립트를 수행합니다.
  스크립트에서 임베드 할 수 있는 객체는 client,user,scope,token_type,claim,type 입니다.
  또한 XMLHttpRequest 객체를 사용하여 외부 통신을 할 수 있습니다.
  
예)XMLHttpRequest

```
var flag = false;
var xh = new XMLHttpRequest();
var result;
var url = "http://...";
xh.onreadystatechange = function(){
    if(xh.readyState == 4 && xh.status == 200){
       var res = xh.responseText;
       var parse = JSON.parse(res);
       log(JSON.stringify(parse));
       var rows = parse['rows'];
       if(rows.length > 0){
         flag = true;
       }
    }
}
return flag;
```

예)임베드 객체 사용

```
if(user.tenant ==! 'KR'){
    return false;
}else{
    return true;
}
```

 - After Use : 프락시 동작 후 실행될 스크립트 입니다. Before Use 와 사용법은 동일하지만, 결과값에 영향을 받지 않으므로 return 을 줄 필요가 없습니다.
 

