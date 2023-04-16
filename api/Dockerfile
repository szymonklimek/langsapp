FROM swaggerapi/swagger-ui:latest
LABEL authors="Szymon Klimek"

COPY public /usr/share/nginx/html/doc
ENV URLS_PRIMARY_NAME="Langsapp Users API"
ENV URLS="\
[\
{url:'doc/users_command_api.yaml',name:'Langsapp Users API'},\
{url:'doc/user_follow_command_api.yaml',name:'Langsapp User Follow API'}\
]"
