#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <stdio.h>
#include <string.h>
#include <X11/bitmaps/escherknot>
 
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
	int screen = DefaultScreen(dpy);
	Visual *visual = DefaultVisual(dpy, screen);
	int depth = DefaultDepth(dpy,screen);
	XSetWindowAttributes attributes;

	/* this variable will contain the ID of the newly created pixmap.    */
	Pixmap bitmap;

	/* these variables will contain the dimensions of the loaded bitmap. */
	unsigned int bitmap_width, bitmap_height;

	/* these variables will contain the location of the hot-spot of the   */
	/* loaded bitmap.                                                    */
	int hotspot_x, hotspot_y;

	/* this variable will contain the ID of the root window of the screen */
	/* for which we want the pixmap to be created.                        */
	Window root_win = DefaultRootWindow(dpy);

	/* load the bitmap found in the file "icon.bmp", create a pixmap      */
	/* containing its data in the server, and put its ID in the 'bitmap'  */
	/* variable.                                                          */
/*	int rc = XReadBitmapFile(dpy, root_win,
		                     "/usr/share/xfig/bitmaps/octagons.bmp", ///usr/share/doc/tk8.4/examples/images/pattern.bmp",
		                     &bitmap_width, &bitmap_height,
		                     &bitmap,
		                     &hotspot_x, &hotspot_y);*/
	/* check for failure or success. */
/*	switch (rc) {
		case BitmapOpenFailed:
		    fprintf(stderr, "XReadBitmapFile - could not open file 'icon.bmp'.\n");
		    break;
		case BitmapFileInvalid:
		    fprintf(stderr,
		            "XReadBitmapFile - file '%s' doesn't contain a valid bitmap.\n",
		            "icon.bmp");
		    break;
		case BitmapNoMemory:
		    fprintf(stderr, "XReadBitmapFile - not enough memory.\n");
		    break;
		case BitmapSuccess:
		    // bitmap loaded successfully - do something with it... 
			printf("Read bitmap file ok width=%d, height=%d\n",bitmap_width, bitmap_height);
		    break;
	}
*/
	bitmap_width = escherknot_width;
	bitmap_height = escherknot_height;
	bitmap = XCreatePixmapFromBitmapData(dpy, root_win, escherknot_bits, bitmap_width, bitmap_height, blackColor, whiteColor, depth);

    // Create the window
    Window w = XCreateSimpleWindow(
		dpy, 
		root_win, 
		0, 0, 400, 400, 50, whiteColor, blackColor);
    XStoreName(dpy, w, "Tiles");
	XSetWindowBorderPixmap(dpy, w, bitmap);

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
