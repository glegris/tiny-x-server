// Written by Ch. Tronche (http://tronche.com/)
// Copyright by the author. This is unmaintained, no-warranty free software. 
// Please use freely. It is appreciated (but by no means mandatory) to
// acknowledge the author's contribution. Thank you.
//
//  Copyright (C) 1998-2000 by Christophe Tronche ("the author"). All
//  Rights Reserved.

//  Permission is hereby granted, free of charge, to any person obtaining
//  a copy of this software and associated documentation files (the
//  "Software"), to deal in the Software without restriction, including
//  without limitation the rights to use, copy, modify, merge, publish,
//  distribute, sublicense, and/or sell copies of the Software, and to
//  permit persons to whom the Software is furnished to do so, subject to
//  the following conditions:

//  The above copyright notice and this permission notice shall be
//  included in all copies or substantial portions of the Software.

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
//  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
//  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//  OTHER DEALINGS IN THE SOFTWARE.
//
// Started on Sat May  6 18:32:40 2000
//
// v1.0, Sat Jun  3 12:05:16 MET 2000
//
// This programs does nothing useful. It was just written so as to generate every X11
// request in order to test the X11 decoder for ethereal (see http://ethereal.zing.org/).
//
// To compile try something like: g++ all-request.cc -lX11 
// or may be g++ all-request.cc -L/usr/X11R6/lib -lX11
//

#include <X11/Xatom.h>
#include <X11/Xlib.h>
#include <X11/keysym.h>
#include <iostream>
#include <assert.h>
#include <cstring>
#include <stdio.h>
#include <unistd.h>

#define NIL (0)

#define SIZEOF(x) (sizeof(x) / (sizeof((x)[0])))

static const char longString[] = "Because our senses sometimes fool us, I decided to believe there was nothing as they made us imagine. (...) And, noticing that this truth: \"I think, therefore I am\", was so firm and so assured that none of the most extravagant suppositions of the Skeptical ones couldn't took it in default, I decided to receive it as the first principle of the philosophy I was searching for. --- René Descartes, discours de la méthode.";

static int errorHandler(Display *dpy, XErrorEvent *error)
{
      char buffer1[1024];
      XGetErrorText(dpy, error -> error_code, buffer1, 1024);
      fprintf(stderr, 
	      "_X Error of failed request:  %s\n"
	      "_  Major opcode of failed request: % 3d (???)\n"
	      "_  Serial number of failed request:% 5d\n"
	      "_  Current serial number in output stream:?????\n",
	      buffer1,
	      error -> request_code,
	      error -> serial);
      return 0;
}

main()
{
      Window w2;

      Display *dpy = XOpenDisplay(NIL);
      assert(dpy);
      Screen *scr = DefaultScreenOfDisplay(dpy);

      // CreateWindow

      Window w = XCreateWindow(dpy, RootWindowOfScreen(scr), 10, 100, 200, 300, 0, CopyFromParent,
			       CopyFromParent, CopyFromParent,
			       0, NIL);

      XDestroyWindow(dpy, w);

      // CreateWindow with arguments

      XSetWindowAttributes swa;
      swa.background_pixel = WhitePixelOfScreen(scr);
      swa.bit_gravity = NorthWestGravity;
      swa.border_pixel = BlackPixelOfScreen(scr);
      swa.colormap = DefaultColormapOfScreen(scr);
      swa.cursor = None;
      swa.win_gravity = NorthGravity;

      w = XCreateWindow(dpy, RootWindowOfScreen(scr), 10, 100, 200, 300, 0, CopyFromParent,
			CopyFromParent, CopyFromParent,
			CWBackPixel | CWBitGravity | CWBorderPixel | CWColormap | CWCursor | CWWinGravity, 
			&swa);
      
      // CreateWindow with other arguments

      XDestroyWindow(dpy, w);

      Pixmap pixmap = XCreatePixmap(dpy, RootWindowOfScreen(scr), 45, 25, DefaultDepthOfScreen(scr));
      assert(pixmap);

      swa.background_pixmap = pixmap;
      swa.border_pixmap = pixmap;

      w = XCreateWindow(dpy, RootWindowOfScreen(scr), 10, 100, 200, 300, 0, CopyFromParent,
			CopyFromParent, CopyFromParent,
			CWBackPixmap | CWBorderPixmap,
			&swa);
      
      // ChangeWindowAttributes

      swa.backing_planes = 0x1;
      swa.backing_pixel = WhitePixelOfScreen(scr);
      swa.save_under = True;
      swa.event_mask = KeyPressMask | KeyReleaseMask;
      swa.do_not_propagate_mask = ButtonPressMask | Button4MotionMask;
      swa.override_redirect = False;
      XChangeWindowAttributes(dpy, w, CWBackingPlanes | CWBackingPixel | CWSaveUnder | CWEventMask
			      | CWDontPropagate | CWOverrideRedirect, &swa);

      // GetWindowAttributes

      XWindowAttributes wa;
      Status success = XGetWindowAttributes(dpy, w, &wa);

      // DestroyWindow (done)

      // DestroySubwindows

      w2 = XCreateWindow(dpy, w, 20, 30, 40, 50, 3, CopyFromParent, CopyFromParent, CopyFromParent, 0, NIL);
      XDestroySubwindows(dpy, w);

      // ChangeSaveSet

//        Display *dpy2 = XOpenDisplay(NIL);
//        assert(dpy2);
//        XAddToSaveSet(dpy2, w);
//        XCloseDisplay(dpy2);

      // ReparentWindow

      w2 = XCreateWindow(dpy, RootWindowOfScreen(scr), 20, 30, 40, 50, 3, 
			 CopyFromParent, CopyFromParent, CopyFromParent, 0, NIL);
      XReparentWindow(dpy, w2, w, 10, 5);

      // MapWindow

      XMapWindow(dpy, w);

      // CreatePixmap

      Pixmap pix2 = XCreatePixmap(dpy, w, 100, 200, DefaultDepthOfScreen(scr));

      // FreePixmap

      XFreePixmap(dpy, pix2);

      // CreateGC

      Pixmap bitmap = XCreateBitmapFromData(dpy, RootWindowOfScreen(scr), 
					    "\000\000\001\000\000\001\000\000\001\377\377\377", 3, 4);

      XGCValues gcv;
      gcv.function = GXand;
      gcv.plane_mask = 0x1;
      gcv.foreground = WhitePixelOfScreen(scr);
      gcv.background = BlackPixelOfScreen(scr);
      gcv.line_width = 2;
      gcv.line_style = LineDoubleDash;
      gcv.cap_style = CapProjecting;
      gcv.join_style = JoinRound;
      gcv.fill_style = FillStippled;
      gcv.fill_rule = EvenOddRule;
      gcv.arc_mode = ArcPieSlice;
      gcv.tile = pixmap;
      gcv.stipple = bitmap;
      gcv.ts_x_origin = 3;
      gcv.ts_y_origin = 4;
//      gcv.font = fs -> fid;
      gcv.subwindow_mode = ClipByChildren;
      gcv.graphics_exposures = True;
      gcv.clip_x_origin = 5;
      gcv.clip_y_origin = 6;
      gcv.clip_mask = bitmap;
      gcv.dash_offset = 1;
      gcv.dashes = 0xc2;
      
      GC gc = XCreateGC(dpy, w, 
			  GCFunction | GCPlaneMask | GCForeground | GCBackground | GCLineWidth
			| GCLineStyle | GCCapStyle | GCJoinStyle | GCFillStyle | GCFillRule | GCTile
			| GCStipple | GCTileStipXOrigin | GCTileStipYOrigin | GCFont | GCSubwindowMode
			| GCGraphicsExposures | GCClipXOrigin | GCClipYOrigin | GCClipMask | GCDashOffset
			| GCDashList | GCArcMode,
			&gcv);

      // ChangeGC

      gcv.function = GXandReverse;

      // Only a few of these should appear, since the values are cached on the client side by the Xlib.

      XChangeGC(dpy, gc, GCFunction | GCLineStyle | GCStipple | GCGraphicsExposures | GCDashList, &gcv);

      // CopyGC
      
      GC gc2 = XCreateGC(dpy, w, 0, NIL);
      XCopyGC(dpy, gc, GCFunction | GCLineStyle | GCStipple | GCGraphicsExposures | GCDashList, gc2);

      // SetDashes

      XSetDashes(dpy, gc, 3, "\001\377\001", 3);

      // SetClipRectangles

      XRectangle rectangles[] = { { 10, 20, 30, 40 }, { 100, 200, 5, 3 }, { -5, 1, 12, 24 } };
      XSetClipRectangles(dpy, gc, 12, 9, rectangles, SIZEOF(rectangles), Unsorted);

      // FreeGC

	    // done already

      // ClearArea

      XClearArea(dpy, w, 30, 10, 10, 100, False);

      // CopyArea

      XCopyArea(dpy, w, pixmap, gc, 0, 0, 100, 100, 10, 10);

      // CopyPlane

      // This won't work if the Screen doesn't have at least 3 planes
      XCopyPlane(dpy, pixmap, w, gc, 20, 10, 40, 30, 0, 0, 0x4);

      // PutImage
      // GetImage

      XImage *image = XGetImage(dpy, w, 10, 20, 40, 30, AllPlanes, ZPixmap);
      XPutImage(dpy, w, gc, image, 0, 0, 50, 60, 40, 30);
      XSync(dpy, False); // Make the next request starts at the beginning of a packet


      XColor screen2, exact;

      // NoOperation

      XNoOp(dpy);

      for(;;) {
	    XEvent e;
	    XNextEvent(dpy, &e);
	    std::cout << "Got an event of type " << e.type << std::endl;
      }
}
