FROM node:21-slim AS build
WORKDIR /frontend
COPY frontend .
RUN npm run build .

FROM nginx:stable-alpine3.17-slim
COPY --from=build /frontend/build /usr/share/nginx/html
COPY docker/nginx/default.conf /etc/nginx/conf.d 
