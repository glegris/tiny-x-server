#include <X11/Xlib.h>
#include <stdio.h>
#include <string.h>
 
#define NIL (0)
 
int main(int argc, char* argv[])
{
        Display *dpy = XOpenDisplay(NIL);
        if(!dpy)
        {
                printf("Failed to open the display.\n");
                return 0;
        }
 
        int blackColor = BlackPixel(dpy, DefaultScreen(dpy));
        int whiteColor = WhitePixel(dpy, DefaultScreen(dpy));
 
		XFontStruct * font1 = XLoadQueryFont (dpy, "-*-Arial-bold-i-*-*-14-*-*-*-*-*-*-*");
		if(!font1) 
		{
                printf("Failed to open font1.\n");
                return 0;
        }
		XFontStruct * font2 = XLoadQueryFont (dpy, "-*-Arial-bold-r-*-*-28-*-*-*-*-*-*-*");
		if(!font2) 
		{
                printf("Failed to open font2.\n");
                return 0;
        }
		XFontStruct * font3 = XLoadQueryFont (dpy, "fixed");
		if(!font3) 
		{
                printf("Failed to open font3.\n");
                return 0;
        }
		XFontStruct * font4 = XLoadQueryFont (dpy, "cursor");
		if(!font4) 
		{
                printf("Failed to open font4.\n");
                return 0;
        }
		XFontStruct * font5 = XLoadQueryFont (dpy, "6x10");
		if(!font4) 
		{
                printf("Failed to open font4.\n");
                return 0;
        }
		XTextItem textItems[2];
		textItems[0].chars = "Hello";
		textItems[0].nchars = strlen(textItems[0].chars);
		textItems[0].delta = 15;
		textItems[0].font = font1->fid;
		textItems[1].chars = "World!";
		textItems[1].nchars = strlen(textItems[1].chars);
		textItems[1].delta = 10;
		textItems[1].font = font2->fid;
		

        // Below you can see how to initalize a different color, it's pretty complicated, but there you go.
        XColor green, exact;
        Colormap cmap = DefaultColormap(dpy, 0);
        XAllocNamedColor(dpy, cmap, "green",&green, &exact);
        int greenColor = green.pixel;
 
        // Create the window
        Window w = XCreateSimpleWindow(dpy, DefaultRootWindow(dpy), 0, 0, 220, 220,40, greenColor, blackColor);
        XStoreName(dpy, w, "Hello World!");
 
 
        // Create a graphics context
        GC gc = XCreateGC(dpy, w, 0, NIL);
 
        // Tell the gc we draw using the white color
        XSetForeground(dpy, gc, whiteColor);
 
         // Send the requests to the server
        XFlush(dpy);
 
        XEvent event;
        XSelectInput(dpy, w, ExposureMask | ButtonPressMask);

        // "Map" the window  (make it appear on the screen)
        XMapWindow(dpy, w);

        while(!XNextEvent(dpy,&event))
        {
                if(event.type == Expose)
                {
                        XClearWindow(dpy, w);
						XSetFont (dpy, gc, font1->fid);
                        XDrawString(dpy, w, gc, 20, 20, "Hello World!", strlen("Hello World!"));
				        XDrawText(dpy, w, gc, 20, 50, &textItems, 2);
				        XSetBackground(dpy, gc, greenColor);
                        XDrawImageString(dpy, w, gc, 20, 100, "Hello World!", strlen("Hello World!"));
						XSetFont (dpy, gc, font3->fid);
                        XDrawString(dpy, w, gc, 20, 130, "Hello World!", strlen("Hello World!"));
						XSetFont (dpy, gc, font4->fid);
                        XDrawString(dpy, w, gc, 20, 150, "Hello World!", strlen("Hello World!"));
 						XSetFont (dpy, gc, font5->fid);
                        XDrawString(dpy, w, gc, 20, 170, "Hello World!", strlen("Hello World!"));
                        // Send the requests to the server
                        XFlush(dpy);
                }
                else if(event.type == ButtonPress)
                {
                        // If you would like to get the coordinates of the mouse, you would create
                        // an XButtonPressedEvent like below, and then use the x and y fields of that event
                        //XButtonPressedEvent *bpEvent = &amp;event.xbutton;
                        break;
                }
        }
        return 0;
}
