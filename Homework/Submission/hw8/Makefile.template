# Generic Makefile for the .tex files in the current directory.
# Copy this file to a homework directory and name it Makefile.

LATEXMK ?= latexmk
LATEXMK_FLAGS ?= -pdf -interaction=nonstopmode -halt-on-error

TEX_FILES := $(wildcard *.tex)
PDF_FILES := $(TEX_FILES:.tex=.pdf)
MAIN ?= $(basename $(firstword $(TEX_FILES)))

.PHONY: all pdf view clean distclean

all: pdf

pdf: $(PDF_FILES)

%.pdf: %.tex
	$(LATEXMK) $(LATEXMK_FLAGS) "$<"

# Any requested basename maps to its matching PDF, which depends on the .tex source.
REQUESTED_TEX := $(filter-out all pdf view clean distclean,$(MAKECMDGOALS))
ifneq ($(strip $(REQUESTED_TEX)),)
.PHONY: $(REQUESTED_TEX)
$(REQUESTED_TEX): %: %.pdf
	@:
endif

view: $(MAIN).pdf
	open "$<"

# Remove LaTeX auxiliary files but keep the PDFs.
clean:
	@if [ -n "$(TEX_FILES)" ]; then $(LATEXMK) -c $(TEX_FILES); fi

# Remove auxiliary files and generated PDFs.
distclean: clean
	@rm -f $(PDF_FILES)
