FROM joyzoursky/python-chromedriver:3.9-selenium

WORKDIR /code

#
COPY ./requirements.txt /code/requirements.txt

#
RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt
#
RUN pip install fastapi uvicorn

COPY ./qr /code/qr

WORKDIR /code/qr
CMD ["uvicorn", "qr:app", "--host", "0.0.0.0", "--port", "8083"]

EXPOSE 8083