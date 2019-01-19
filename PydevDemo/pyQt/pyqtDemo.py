import sys
from PyQt5 import QtGui
from PyQt5.QtCore import (Qt, pyqtSignal)
from PyQt5.QtWidgets import (QWidget, QLCDNumber, QSlider,
    QVBoxLayout, QApplication, QLabel)
from PyQt5.Qt import QRect


class Example(QWidget):
    
    slider_value=0
    
    def __init__(self):
        super().__init__()
        self.initUI()

    def printLabel(self, str):
        print(str)

    def logLabel(self, str):
        '''log to a file'''
        pass
    
    def label(self,value):
        self.value_label.setText(str(value))

    def detect_direction(self,value):
        print(self.slider_value)
        if self.slider_value==0:
            self.direction_label.setText('Increasing')
        elif self.slider_value>value:
            self.direction_label.setText('Decreasing')
        else:
            self.direction_label.setText('Increasing')
        self.slider_value=value
            

    def initUI(self):

        lcd = QLCDNumber(self)
        sld = QSlider(Qt.Horizontal, self)
        
        new_font = QtGui.QFont("Times", 20, QtGui.QFont.DemiBold)
        
        self.value_label=QLabel(self)
        self.value_label.setFont(new_font)
        self.value_label.setText('0')

        self.direction_label=QLabel(self)
        self.direction_label.setFont(new_font)
        self.direction_label.setText('Start')


        vbox = QVBoxLayout()
        vbox.addWidget(lcd)
        vbox.addWidget(sld)
        vbox.addWidget(self.value_label)
        vbox.addWidget(self.direction_label)
        self.setLayout(vbox)

        #redundant connections
        sld.valueChanged.connect(lcd.display)
        sld.valueChanged.connect(self.printLabel)
        sld.valueChanged.connect(self.logLabel)
        sld.valueChanged.connect(self.label)
        sld.valueChanged.connect(self.detect_direction)

        self.setGeometry(300, 300, 350, 250)
        self.setWindowTitle('Signal & slot')
        self.show()


if __name__ == '__main__':

    app = QApplication(sys.argv)
    ex = Example()
    sys.exit(app.exec_())