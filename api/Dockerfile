FROM swaggerapi/swagger-ui:latest
LABEL authors="Szymon Klimek"

COPY public /usr/share/nginx/html/doc
ENV URLS_PRIMARY_NAME="Langsapp Users API"
ENV URLS="\
[\
{url:'doc/user_command_api.yaml',name:'Langsapp User API'},\
{url:'doc/user_follow_command_api.yaml',name:'Langsapp User Follow API'}\
]"
