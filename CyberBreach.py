import random
from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label
from kivy.uix.textinput import TextInput
from kivy.uix.button import Button
from kivy.core.window import Window
from kivy.utils import get_color_from_hex
from kivy.clock import Clock

# ستايل هكر متطور
Window.clearcolor = get_color_from_hex('#050505')
Window.size = (450, 650)

class CyberBreach(App):
    def build(self):
        self.title = "CYBER BREACH: MEMORY PROTOCOL"
        self.level = 1
        self.lives = 3
        self.high_score = 1
        self.target_code = ""
        
        # الحاوية الرئيسية
        self.root = BoxLayout(orientation='vertical', padding=25, spacing=15)

        # 1. لوحة الحالة (Status Bar)
        status_layout = BoxLayout(size_hint=(1, 0.1))
        self.lives_label = Label(text=f"LIVES: {'❤️' * self.lives}", color=get_color_from_hex('#FF5555'), bold=True)
        self.hs_label = Label(text=f"BEST: Lvl {self.high_score}", color=get_color_from_hex('#FACC15'))
        status_layout.add_widget(self.lives_label)
        status_layout.add_widget(self.hs_label)
        self.root.add_widget(status_layout)

        # 2. الشاشة المركزية (The Console)
        self.console = Label(
            text="[ SYSTEM READY ]\nPRESS START TO BREACH",
            font_size='22sp',
            bold=True,
            color=get_color_from_hex('#00FF41'),
            halign='center'
        )
        self.root.add_widget(self.console)

        # 3. حقل إدخال الكود
        self.input = TextInput(
            hint_text="ENTER DECRYPT KEY...",
            multiline=False,
            font_size='24sp',
            size_hint=(1, 0.15),
            background_color=(0, 0.1, 0, 1),
            foreground_color=(0, 1, 0, 1),
            disabled=True,
            opacity=0.2
        )
        self.input.bind(on_text_validate=self.verify_key)
        self.root.add_widget(self.input)

        # 4. زر التحكم
        self.action_btn = Button(
            text="INITIALIZE BREACH",
            size_hint=(1, 0.15),
            background_normal='',
            background_color=get_color_from_hex('#003300'),
            color=(0, 1, 0, 1),
            bold=True
        )
        self.action_btn.bind(on_press=self.next_round)
        self.root.add_widget(self.action_btn)

        return self.root

    def next_round(self, instance):
        self.action_btn.disabled = True
        self.action_btn.opacity = 0
        self.input.text = ""
        self.input.disabled = True
        self.input.opacity = 0.2
        
        # توليد كود (أرقام + حروف) بيصعب مع الليفل
        chars = "0123456789ABCDEF" if self.level < 5 else "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#"
        length = self.level + 2
        self.target_code = "".join(random.choice(chars) for _ in range(length))
        
        self.console.text = "DECRYPTING SEQUENCE..."
        Clock.schedule_once(self.flash_code, 1.2)

    def flash_code(self, dt):
        self.console.text = self.target_code
        self.console.color = get_color_from_hex('#FFFFFF')
        # وقت الظهور يقل تدريجياً
        show_time = max(0.4, 2.5 - (self.level * 0.2))
        Clock.schedule_once(self.hide_code, show_time)

    def hide_code(self, dt):
        self.console.text = "INPUT KEY NOW"
        self.console.color = get_color_from_hex('#00FF41')
        self.input.disabled = False
        self.input.opacity = 1
        self.input.focus = True

    def verify_key(self, instance):
        if self.input.text.upper() == self.target_code:
            self.level += 1
            if self.level > self.high_score: self.high_score = self.level
            self.console.text = f"SUCCESS!\nLEVEL {self.level} UNLOCKED"
            self.hs_label.text = f"BEST: Lvl {self.high_score}"
            Clock.schedule_once(self.next_round, 1.2)
        else:
            self.lives -= 1
            self.lives_label.text = f"LIVES: {'❤️' * self.lives}"
            if self.lives > 0:
                self.console.text = "WRONG KEY!\nRETRYING..."
                self.trigger_glitch('#FF3333')
                Clock.schedule_once(self.next_round, 1.2)
            else:
                self.game_over()

    def trigger_glitch(self, color):
        orig = Window.clearcolor
        Window.clearcolor = get_color_from_hex(color)
        Clock.schedule_once(lambda dt: setattr(Window, 'clearcolor', orig), 0.1)

    def game_over(self):
        self.console.text = "SYSTEM LOCKDOWN\nACCESS DENIED"
        self.console.color = get_color_from_hex('#FF0000')
        self.action_btn.disabled = False
        self.action_btn.opacity = 1
        self.action_btn.text = "REBOOT SYSTEM"
        self.level = 1
        self.lives = 3
        self.lives_label.text = f"LIVES: {'❤️' * self.lives}"

if __name__ == '__main__':
    CyberBreach().run()