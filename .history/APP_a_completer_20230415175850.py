# -*- coding: utf-8 -*-
import shutil
import os
import io
import sys
import json
import folium
from folium.plugins.draw import Draw
import pandas as pd
from PyQt5.QtWidgets import QApplication, QVBoxLayout, QWidget  # , QFileDialog
from PyQt5.QtWebEngineWidgets import QWebEngineView
import requests


class CodificationPalabre():
    def __init__(self,  geojsonFile):
        self.geojsonfile = geojsonFile

    def createDelimitation():
        pass


class Mapy(QWidget):
    def __init__(self, name: str = "data", parent=None):
        super(Mapy, self).__init__(parent)
        self.temp_file = "metadata"
        self.save_file = f"{name}.geojson"
        self.interfejs()

    def interfejs(self):
        vbox = QVBoxLayout(self)
        self.webEngineView = QWebEngineView()
        self.webEngineView.page().profile().downloadRequested.connect(
            self.handle_downloadRequested
        )

        self.loadPage()
        vbox.addWidget(self.webEngineView)
        self.setLayout(vbox)
        self.setGeometry(700, 700, 700, 700)
        self.setWindowTitle("TP")
        self.show()

    def loadPage(self):
        # , crs="EPSG3163")
        m = folium.Map(location=[-21.6166700, 166.2166700], zoom_start=17)

        Draw(
            export=True,
            filename=self.temp_file,
            position="topleft",
            draw_options={
                "polyline": False,
                "rectangle": False,
                "circle": False,
                "circlemarker": False,
            },
            edit_options={"poly": {"allowIntersection": False}},
        ).add_to(m)

        folium.TileLayer(
            tiles='https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
            attr='Esri',
            name='Esri Satellite',
            overlay=False,
            control=True
        ).add_to(m)

        folium.LatLngPopup().add_to(m)

        data = io.BytesIO()
        m.save(data, close_file=False)

        self.webEngineView.setHtml(data.getvalue().decode())

    def load_data(self):
        datas = pd.read_json(self.temp_file)
        shutil.copy2(self.temp_file, self.save_file)
        os.remove(self.temp_file)

        my_list = []
        for gpsPoint in datas['features']:
            my_list.append(gpsPoint["geometry"]["coordinates"])

        self.my_coords = my_list
        with open(f"list_{self.save_file}", "w") as f:
            f.write(json.dumps(my_list, indent=2))

    def handle_downloadRequested(self, item):
        item.setPath(self.save_file)
        item.accept()
        datas = pd.read_json(self.save_file)
        print("Modification du fichier data.geojson effectuée !")

        #my_list = []
        # for gpsPoint in datas['features'] :
        #    print("point", gpsPoint["geometry"]["coordinates"] )
        #    my_list.append(gpsPoint["geometry"]["coordinates"] )

    def send_data_geoson_to_API_SB():
        # Open the GeoJSON file in read mode
        with open('./data.geojson', 'r') as f:
            # Load the GeoJSON file contents into a Python variable
            geojson_data = json.load(f)

        # Extract the coordinates from the GeoJSON file
        coordinates = geojson_data['features'][0]['geometry']['coordinates'][0]
        coordinates.pop()
        url = 'localhost:8080/smart-contrat-baux-ruraux'
        payload = {'coordinates': coordinates}
        response = requests.post(url, json=payload)
        if response.status_code == 200:
            print('Coordinates sent successfully!')
            print(response.content)
        else:
            print('Error sending coordinates')
            print("La requête a échouée avec le code de retour {} et le message {}.".format(
                response.status_code, response.content))


if __name__ == "__main__":
    app = QApplication(sys.argv)
    #name = input("Nom de la personne : ")
    #name = f"{name}"
    name = "data"
    okno = Mapy(name=name)
    sys.exit(app.exec_())
