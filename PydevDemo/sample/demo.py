import sys
from PyQt5.QtWidgets import QApplication, QWidget, QMessageBox

class App(QWidget):
 
    def __init__(self):
        super().__init__()
        self.title = 'PyQt5 Msg Box'
        self.left = 500
        self.top = 500
        self.width = 500
        self.height = 500
        self.initUI()
 
    def initUI(self):
        self.setWindowTitle(self.title)
        self.setGeometry(self.left, self.top, self.width, self.height)
 
        buttonReply = QMessageBox.question(self, 'PyQt5 message', "Do you like PyQt5?", QMessageBox.Yes | QMessageBox.No|QMessageBox.Abort, QMessageBox.No)
        if buttonReply == QMessageBox.Yes:
            print('Yes clicked.')  
        elif buttonReply == QMessageBox.No:
            print('No clicked.')
        else:
            print('abort clicked.')
 
        self.show()
 
if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = App()
    #print(ex.__str__())
    sys.exit(app.exec_())
    