FROM swaggerapi/swagger-ui:latest
LABEL authors="Szymon Klimek"

COPY public /usr/share/nginx/html/doc
ENV URLS_PRIMARY_NAME="Langsapp Users API"
ENV URLS="\
[\
{url:'doc/user_commands_api.yaml',name:'Langsapp User Commands API'},\
{url:'doc/user_follow_commands_api.yaml',name:'Langsapp User Follow Commands API'},\
{url:'doc/user_profile_query_api.yaml',name:'Langsapp User Profile Query API'}\
]"
